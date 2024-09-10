package online.mayak.energy.ocpp16;

import java.io.IOException;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.exception.OcppErrorCode;
import online.mayak.energy.exception.OcppException;
import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;

@Component
@RequiredArgsConstructor
public class OcppMessageSerializer {

	private final static String CALL_MESSAGE_FORMAT = "[%s,\"%s\",\"%s\",%s]";
	private final static String CALL_RESULT_MESSAGE_FORMAT = "[%s,\"%s\",%s]";
	private final static String CALL_ERROR_MESSAGE_FORMAT = "[%s,\"%s\",\"%s\",\"%s\",%s]";

	private final ObjectMapper objectMapper;
	private final JsonSchemaValidator jsonValidator;

	// Serialization

	public String serialize(OcppCallMessage message) {
		try {
			return String.format(CALL_MESSAGE_FORMAT,
					// MessageTypeId
					message.getMessageTypeId(),
					// MessageId (UniqueId)
					message.getMessageId(),
					// Action
					message.getAction(),
					// Payload
					message.getPayload() != null ? objectMapper.writeValueAsString(message.getPayload()) : "{}");
		} catch (Exception e) {
			throw new OcppException(String.format("Serialization error: %s", e.getMessage()), e);
		}
	}

	public String serialize(OcppCallResultMessage message) {
		try {
			return String.format(CALL_RESULT_MESSAGE_FORMAT,
					// MessageTypeId
					message.getMessageTypeId(),
					// MessageId (UniqueId)
					message.getMessageId(),
					// Payload
					message.getPayload() != null ? objectMapper.writeValueAsString(message.getPayload()) : "{}");
		} catch (Exception e) {
			throw new OcppException(String.format("Serialization error: %s", e.getMessage()), e);
		}
	}

	public String serialize(OcppCallErrorMessage message) {
		try {
			return String.format(CALL_ERROR_MESSAGE_FORMAT,
					// MessageTypeId
					message.getMessageTypeId(),
					// MessageId (UniqueId)
					message.getMessageId(),
					// ErrorCode
					message.getErrorCode(),
					// ErrorDescription
					message.getErrorDescription() != null ? message.getErrorDescription() : "",
					// ErrorDetails
					message.getErrorDetails() != null ? objectMapper.writeValueAsString(message.getErrorDetails()) : "{}");
		} catch (Exception e) {
			throw new OcppException(String.format("Serialization error: %s", e.getMessage()), e);
		}
	}

	// Deserialization

	public int extractMessageTypeId(String message) {
		try (JsonParser parser = objectMapper.getFactory().createParser(message)) {
			parser.nextToken(); // '['
			parser.nextToken(); // MessageTypeId
			return parser.getValueAsInt();
		} catch (IOException e) {
			throw new OcppException(String.format("Deserialization error: %s", e.getMessage()), e);
		}
	}

	public String extractMessageId(String message) {
		try (JsonParser parser = objectMapper.getFactory().createParser(message)) {
			parser.nextToken(); // '['
			parser.nextToken(); // MessageTypeId
			parser.nextToken(); // MessageId (UniqueId)
			return parser.getValueAsString();
		} catch (IOException e) {
			throw new OcppException(String.format("Deserialization error: %s", e.getMessage()), e);
		}
	}

	private OcppMessage deserializeOcppMessage(JsonParser parser) {
		try {
			// Распарсить общую часть сообщения
			parser.nextToken(); // '['
			parser.nextToken(); // MessageTypeId
			int messageTypeId = parser.getValueAsInt();
			parser.nextToken(); // MessageId (UniqueId)
			String messageId = parser.getValueAsString();
			return new OcppMessage(messageTypeId, messageId);
		} catch (Exception e) {
			throw new OcppException(String.format("Deserialization error: %s", e.getMessage()), e);
		}
	}

	public OcppCallMessage deserializeOcppCallMessage(String message) {
		try (JsonParser parser = objectMapper.getFactory().createParser(message)) {
			// Распарсить общую часть сообщения
			OcppMessage ocppMessage = deserializeOcppMessage(parser);
			// Дораспарсить сообщение
			parser.nextToken(); // Action
			String actionName = parser.getValueAsString();
			parser.nextToken(); // Payload
			JsonNode payloadNode = parser.readValueAsTree();
			// Получить Action
			Action action = Action.valueOfOrNull(actionName);
			if (action == null)
				throw new OcppException(OcppErrorCode.NotImplemented, String.format("Unregistered Action: %s", actionName));
			// Провалидировать тело запроса
			Set<ValidationMessage> errors = jsonValidator.validateRequest(action, payloadNode);
			if (!errors.isEmpty())
				throw new OcppException(OcppErrorCode.FormationViolation, "Deserialization error", errors);
			// Десериализовать запрос
			ActionRequest actionRequest = objectMapper.treeToValue(payloadNode, action.requestType());
			// Получить OcppCallMessage
			return OcppCallMessage.builder()
					.messageId(ocppMessage.getMessageId())
					.action(action)
					.payload(actionRequest)
					.build();
		} catch (IOException e) {
			throw new OcppException(String.format("Deserialization error: %s", e.getMessage()), e);
		}
	}

	public OcppCallResultMessage deserializeOcppCallResultMessage(String message, Action action) {
		try (JsonParser parser = objectMapper.getFactory().createParser(message)) {
			// Распарсить общую часть сообщения
			OcppMessage ocppMessage = deserializeOcppMessage(parser);
			// Дораспарсить сообщение
			parser.nextToken(); // Payload
			JsonNode payloadNode = parser.readValueAsTree();
			// Провалидировать тело ответа
			Set<ValidationMessage> errors = jsonValidator.validateResponse(action, payloadNode);
			if (!errors.isEmpty())
				throw new OcppException(OcppErrorCode.FormationViolation, "Deserialization error", errors.toString());
			// Десериализовать запрос
			ActionResponse actionRequest = objectMapper.treeToValue(payloadNode, action.responseType());
			// Получить OcppCallResultMessage
			return OcppCallResultMessage.builder()
					.messageId(ocppMessage.getMessageId())
					.payload(actionRequest)
					.build();
		} catch (IOException e) {
			throw new OcppException(String.format("Deserialization error: %s", e.getMessage()), e);
		}
	}

	public OcppCallErrorMessage deserializeOcppCallErrorMessage(String message) {
		try (JsonParser parser = objectMapper.getFactory().createParser(message)) {
			// Распарсить общую часть сообщения
			OcppMessage ocppMessage = deserializeOcppMessage(parser);
			// Дораспарсить сообщение
			parser.nextToken(); // ErrorCode
			String errorCodeName = parser.getValueAsString();
			parser.nextToken(); // ErrorDescription
			String errorDescription = parser.getValueAsString();
			parser.nextToken(); // ErrorDetails
			//JsonNode errorDetails = parser.readValueAsTree();
			String errorDetails = parser.getValueAsString();
			// Получить OcppErrorCode
			OcppErrorCode errorCode = OcppErrorCode.valueOf(errorCodeName);
			// Получить OcppCallErrorMessage
			return OcppCallErrorMessage.builder()
					.messageId(ocppMessage.getMessageId())
					.errorCode(errorCode)
					.errorDescription(errorDescription)
					.errorDetails(errorDetails)
					.build();
		} catch (IOException e) {
			throw new OcppException(String.format("Deserialization error: %s", e.getMessage()), e);
		}
	}

}
