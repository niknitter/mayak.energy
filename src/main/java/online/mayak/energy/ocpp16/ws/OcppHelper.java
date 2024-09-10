package online.mayak.energy.ocpp16.ws;

import java.net.URI;
import java.util.Base64;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OcppHelper {

	public static final String CHARGE_POINT_ID_ATTRIBUTE = "CHARGE_POINT_ID";

	// Utils

	public static String getChargePointIdFromURI(URI uri) {
		String[] pathSegments = uri.getPath().split("/");
		return pathSegments[pathSegments.length - 1];
	}
	
	public static String getChargePointBasicAuthorization(String id, String password) {
		String valueToEncode = String.format("%s:%s", id, password);
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}

	public static String getChargePointIdFromSession(WebSocketSession session) {
		return (String) session.getAttributes().get(CHARGE_POINT_ID_ATTRIBUTE);
	} 

	public static String putChargePointIdToAttributes(Map<String, Object> sessionAttributes, String chargePointId) {
		return (String) sessionAttributes.put(CHARGE_POINT_ID_ATTRIBUTE, chargePointId);
	} 

	// Logging
	public static void logConnectionEstablished(WebSocketSession session) {
		log.info("[chargePointId: {}] Connection is established (session: {})", getChargePointIdFromSession(session), session.getId());
	}

	public static void logConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
		log.info("[chargePointId: {}] Connection is closed (session: {}) with status: {}", getChargePointIdFromSession(session), session.getId(), closeStatus);
	}

	public static void logPingSending(WebSocketSession session) {
		log.debug("[chargePointId: {}] Ping sending (session: {})", getChargePointIdFromSession(session), session.getId());
	}

	public static void logPingReceived(WebSocketSession session) {
		log.debug("[chargePointId: {}] Pong received (session: {})", getChargePointIdFromSession(session), session.getId());
	}

	public static void logPingError(WebSocketSession session, Throwable t) {
		log.error("[chargePointId: {}] Ping error (session: {}): {}", getChargePointIdFromSession(session), session.getId(), t.getMessage(), t);
	}

	public static void logMessageSending(WebSocketSession session, String payload) {
		log.info("[chargePointId: {}] Message sending: {}", getChargePointIdFromSession(session), payload);
	}

	public static void logMessageRceived(WebSocketSession session, String payload) {
		log.info("[chargePointId: {}] Message received: {}", getChargePointIdFromSession(session), payload);
	}

	public static void logOcppError(WebSocketSession session, String message) {
		log.error("[chargePointId: {}] OCPP error (session: {}): {}", getChargePointIdFromSession(session), session.getId(), message);
	}

	public static void logOcppError(WebSocketSession session, Throwable t) {
		log.error("[chargePointId: {}] OCPP error (session: {}): {}", getChargePointIdFromSession(session), session.getId(), t.getMessage(), t);
	}

}
