package online.mayak.energy.ocpp16.ws;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mayak.energy.exception.OcppException;
import online.mayak.energy.ocpp16.Action;
import online.mayak.energy.ocpp16.OcppCallErrorMessage;
import online.mayak.energy.ocpp16.OcppCallMessage;
import online.mayak.energy.ocpp16.OcppCallResultMessage;
import online.mayak.energy.ocpp16.OcppMessage;
import online.mayak.energy.ocpp16.OcppMessageSerializer;
import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class OcppWebSocketHandler extends TextWebSocketHandler {

	private final OcppMessageSerializer serializer;
	private final SessionContextStore sessionContextStore;
	private final RequestContextStore requestContextStore;
	private final OcppCallMessageHandler ocppCallMessageHandler;
	private final WebSocketSender webSocketSender; 

	private final ScheduledExecutorService scheduledExecutorService;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		OcppHelper.logConnectionEstablished(session);
		sessionContextStore.add(OcppHelper.getChargePointIdFromSession(session), session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		OcppHelper.logConnectionClosed(session, status);
		requestContextStore.remove(OcppHelper.getChargePointIdFromSession(session));
		sessionContextStore.remove(OcppHelper.getChargePointIdFromSession(session), session);
	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		OcppHelper.logPingReceived(session);
	}

	/**
	 * Метод принимает любые текстовые сообщение WinSocket, это может быть:
	 * - входящий запрос от устройства
	 * - входящий ответ от устройства на ранее отправленный запрос устройству
	 * - возможно какое-то несанкционированное сообщение.
	 * Если возникает какая-то ошибка, на данном этапе не понятно как на неё реагировать.
	 * Если ошибка возникает при входящем запросе от устройства, то, возможно, на неё нужно отправить ответ с ошибкой. 
	 * Если ошибка возникает при входящем ответе от устройства, то ответ устройству не требуется.
	 * Поэтому на данном этапе следует попытаться разобрать MessageTypeId и MessageId и в случае ошибки просто её залогировать.
	 * Полную же десериализацию с валидацией производить в отдельном методе для каждого типа сообщения (CALL, CALLRESULT, CALLERROR)
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		OcppHelper.logMessageRceived(session, message.getPayload());
		String chargePointId = OcppHelper.getChargePointIdFromSession(session);
		int messageTypeId = serializer.extractMessageTypeId(message.getPayload());
		String messageId = serializer.extractMessageId(message.getPayload());
		switch(messageTypeId) {
		case OcppMessage.MESSAGE_TYPE_ID_CALL:
			ocppCallMessageHandler.handleOcppCallMessage(message.getPayload(), chargePointId, messageId, session);
			break;
		case OcppMessage.MESSAGE_TYPE_ID_CALLRESULT:
			handleOcppCallResultMessage(message.getPayload(), chargePointId, messageId);
			break;
		case OcppMessage.MESSAGE_TYPE_ID_CALLERROR:
			handleOcppCallErrorMessage(message.getPayload(), chargePointId, messageId);
			break;
		default:
			throw new OcppException(String.format("Unregistered MessageTypeId: %d", messageTypeId));
		}
	}

	private void handleOcppCallResultMessage(String message, String chargePointId, String messageId) throws Exception {
		try {
			RequestContext requestContext = requestContextStore.get(chargePointId, messageId);
			try {
				OcppCallResultMessage ocppMessage = serializer.deserializeOcppCallResultMessage(message, requestContext.getAction());
				// Передать ответ во Future
				requestContext.getFutureActionResponse().complete(ocppMessage.getPayload());
			} finally {
				requestContextStore.remove(chargePointId, messageId);
			}
		} catch (Exception e) {
			log.error("Incomming response handle error: {}", e.getMessage(), e);
		}
	}

	private void handleOcppCallErrorMessage(String message, String chargePointId, String messageId) throws Exception {
		try {
			RequestContext requestContext = requestContextStore.get(chargePointId, messageId);
			try {
				OcppCallErrorMessage ocppMessage = serializer.deserializeOcppCallErrorMessage(message);
				// Передать ошибку во Future
				requestContext.getFutureActionResponse().completeExceptionally(
						new OcppException(
								ocppMessage.getErrorCode(),
								ocppMessage.getErrorDescription(),
								ocppMessage.getErrorDetails()));
			} finally {
				requestContextStore.remove(chargePointId, messageId);
			}
		} catch (Exception e) {
			log.error("Incomming response handle error: {}", e.getMessage(), e);
		}
	}

	/**
	 * См.
	 * https://stackoverflow.com/questions/53489023/java-websocket-get-response-outside-of-listener/53490014#53490014
	 * https://www.baeldung.com/java-completablefuture-timeout
	 */
	public CompletableFuture<ActionResponse> sendActionRequest(String chargePointId, Action action, ActionRequest actionRequest, int timeoutMillis) throws IOException {
		// Получить SessionContext
		SessionContext sessionContext = sessionContextStore.get(chargePointId);
		// Сформировать CALL
		OcppCallMessage ocppMessage = OcppCallMessage.builder()
				.messageId(sessionContext.getNextMessageId())
				.action(action)
				.payload(actionRequest)
				.build();
		// Future для записи результата в будущем
		CompletableFuture<ActionResponse> actionResponseFuture = new CompletableFuture<ActionResponse>();
		requestContextStore.add(chargePointId, ocppMessage.getMessageId(), action, actionRequest, actionResponseFuture);
		try {
			// Отправить запрос
			webSocketSender.sendTextMessage(sessionContext.getSession(), new TextMessage(serializer.serialize(ocppMessage)));
			// Создать задание на timeout ответа
			scheduledExecutorService.schedule((() -> {
				if(actionResponseFuture.isDone())
					return;
				try {
					Throwable ex = new TimeoutException(String.format("Action response timeout after %d", timeoutMillis));
					actionResponseFuture.completeExceptionally(ex);
					OcppHelper.logOcppError(sessionContext.getSession(), ex.getMessage());
				} finally {
					requestContextStore.remove(chargePointId, ocppMessage.getMessageId());
				}
			}), timeoutMillis, TimeUnit.MILLISECONDS);
			return actionResponseFuture;
		} catch (Exception e) {
			requestContextStore.remove(chargePointId, ocppMessage.getMessageId());
			throw e;
		}
	}

}
