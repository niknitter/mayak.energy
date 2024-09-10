package online.mayak.energy.ocpp16.ws;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.exception.OcppErrorCode;
import online.mayak.energy.exception.OcppException;
import online.mayak.energy.ocpp16.OcppCallErrorMessage;
import online.mayak.energy.ocpp16.OcppCallMessage;
import online.mayak.energy.ocpp16.OcppCallResultMessage;
import online.mayak.energy.ocpp16.OcppMessageSerializer;
import online.mayak.energy.ocpp16.api.ActionHandler;
import online.mayak.energy.ocpp16.api.ActionHandlerFactory;
import online.mayak.energy.ocpp16.model.ActionResponse;

@Component
@RequiredArgsConstructor
public class OcppCallMessageHandler {

	private final OcppMessageSerializer serializer;
	private final ActionHandlerFactory actionHandlerFactory;
	private final WebSocketSender webSocketSender;

	@Transactional
	public void handleOcppCallMessage(String message, String chargePointId, String messageId, WebSocketSession session) throws Exception {
		try {
			OcppCallMessage ocppMessage = serializer.deserializeOcppCallMessage(message);
			// Получить обработчик запроса
			ActionHandler actionRequestHandler = actionHandlerFactory.getActionHandler(ocppMessage.getAction());
			if (actionRequestHandler == null)
				throw new OcppException(OcppErrorCode.NotImplemented, String.format("Unregistered request handler for action: %s", ocppMessage.getAction()));
			// Обработать ActionRequest, получить ActionResponse
			ActionResponse actionResponse = actionRequestHandler.handleRequest(chargePointId, ocppMessage.getPayload());
			// Создать ответ CALLRESULT
			OcppCallResultMessage ocppCallResultMessage = OcppCallResultMessage.builder()
					.messageId(ocppMessage.getMessageId())
					.payload(actionResponse)
					.build();
			// Отправить устройству
			webSocketSender.sendTextMessage(session, new TextMessage(serializer.serialize(ocppCallResultMessage)));
		} catch (Exception e) {
			OcppHelper.logOcppError(session, e);
			// В случае общем ошибки ответить кодом InternalError
			OcppErrorCode errorCode = OcppErrorCode.InternalError;
			String errorDescription = OcppErrorCode.InternalError.toString();
			Object errorDetails = null;
			// В случае OCPP-ошибки ответить соответствующим кодом и описанием
			if(e instanceof OcppException) {
				OcppException ocppEx = (OcppException) e;
				errorCode = ocppEx.getCode();
				errorDescription = ocppEx.getDescription();
				errorDetails = ocppEx.getDetails();
			}
			// Создать ответ CALLERROR
			OcppCallErrorMessage ocppCallErrorMessage = OcppCallErrorMessage.builder()
					.messageId(messageId)
					.errorCode(errorCode)
					.errorDescription(errorDescription)
					.errorDetails(errorDetails)
					.build();
			// Отправить устройству ошибку
			webSocketSender.sendTextMessage(session, new TextMessage(serializer.serialize(ocppCallErrorMessage)));
		}
	}

}
