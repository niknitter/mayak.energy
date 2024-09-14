package online.mayak.energy.ocpp16.ws;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mayak.energy.config.OcppProperties;
import online.mayak.energy.entity.Charger;
import online.mayak.energy.service.ChargerService;

@Slf4j
@Component
@RequiredArgsConstructor
public class OcppHandshakeHandler implements HandshakeHandler {

	// Список поддерживаемых протоколов OCPP
	private final static List<String> OCPP_VERSIONS = Arrays.asList("ocpp1.6");

	// DefaultHandshakeHandler
	private final HandshakeHandler delegate = new DefaultHandshakeHandler();

	private final OcppProperties ocppProperties;
	private final ChargerService chargerService;

	/**
	 * Handshake Headers [
	 * upgrade:"websocket", 
	 * host:"192.168.65.254",
	 * connection:"Upgrade", 
	 * sec-websocket-version:"13",
	 * sec-websocket-key:"6Hv9lRq2tNjkJicz3876iQ==",
	 * sec-websocket-protocol:"ocpp1.6", 
	 * authorization:"Basic d2FsbGJveDphdXRob3JpemF0aW9uS2V5"]
	 */
	@Override
	public boolean doHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes)
			throws HandshakeFailureException {
		log.info("Handshake request from {} to {}: headers: {}, attributes: {}", request.getRemoteAddress(), request.getURI(), request.getHeaders(), attributes);
		// WebSocklet headers
		WebSocketHttpHeaders requestHeaders = new WebSocketHttpHeaders(request.getHeaders());
		// Проверить наличие поддерживаемых протоколов OCPP в запросе
		List<String> requestProtocols = requestHeaders.getSecWebSocketProtocol();
		if (requestProtocols.isEmpty()) {
			log.error("Protocol version not specified");
			response.setStatusCode(HttpStatus.BAD_REQUEST);
			return false;
		}
		// Проверить поддерживаемые протоколы OCPP
		List<String> supportedProtocols = OCPP_VERSIONS.stream()
				.filter(requestProtocols::contains)
				.collect(Collectors.toList());
		if (supportedProtocols.isEmpty()) {
			log.error("Protocol version is not supported: {}", requestProtocols);
			response.setStatusCode(HttpStatus.BAD_REQUEST);
			return false;
		}
		// Добавить в ответ поддерживаемые протоколы
		response.getHeaders().set("Sec-WebSocket-Protocol", String.join(",", supportedProtocols));
		// Извлечь идентификатор зарядной станции из URI
		String chargePointId = OcppHelper.getChargePointIdFromURI(request.getURI());
		// Проверить наличие зарядной станции в системе
		Charger charger = chargerService.findByChargePointIdOrNull(chargePointId);
		// Если такой зарядной станции нет, то создать или вернуть 404 (см. ocppProperties)
		if (charger == null) {
			log.warn("Charge Point {} is not registered", chargePointId);
			if(ocppProperties.getAutoRegisterNewChargePoint()) {
				charger = new Charger();
				charger.setCreated(LocalDateTime.now());
				charger.setChargePointId(chargePointId);
				charger = chargerService.saveAndFlush(charger);
				log.info("New Charge Point {} was registered automatically", chargePointId);
			} else {
				log.error("Charge Point {} is not registered", chargePointId);
				response.setStatusCode(HttpStatus.NOT_FOUND);
				return false;
			}
		}
		// Авторизация
		String requestAuthorization = requestHeaders.getFirst("authorization");
		if(requestAuthorization != null) {
			// TODO какие бывают авторизации у зарядных станций кроме Basic?
			String basicAuthorization = OcppHelper.getChargePointBasicAuthorization(charger.getChargePointId(), charger.getChargePointId()); // TODO
			 if(!requestAuthorization.equals(basicAuthorization)) {
				 log.error("Charge Point {} authorized failed", chargePointId);
				 response.setStatusCode(HttpStatus.UNAUTHORIZED);
				 return false;
			 }
		}
		// Положить в сессию идентификатор зарядной станции
		OcppHelper.putChargePointIdToAttributes(attributes, chargePointId);
		// Передать управление DefaultHandshakeHandler
		return delegate.doHandshake(request, response, wsHandler, attributes);
	}

}
