package online.mayak.energy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.ocpp16.ws.OcppHandshakeHandler;
import online.mayak.energy.ocpp16.ws.OcppWebSocketHandler;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

	private final OcppHandshakeHandler ocppHandshakeHandler;
	private final OcppWebSocketHandler ocppWebSocketHandler;

	/**
	 * ws://192.168.65.254:8080/ws/ocpp16
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry
				.addHandler(ocppWebSocketHandler, "/ws/ocpp16/*")
				.setHandshakeHandler(ocppHandshakeHandler)
				.setAllowedOrigins("*");
	}

}
