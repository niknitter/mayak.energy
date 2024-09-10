package online.mayak.energy.ocpp16.ws;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSender {

	public void sendTextMessage(WebSocketSession session, TextMessage message) throws IOException {
		OcppHelper.logMessageSending(session, message.getPayload());
		session.sendMessage(message);
	}

}
