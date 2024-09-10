package online.mayak.energy.ocpp16.ws;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.socket.WebSocketSession;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class SessionContext {

	private final static int MESSAGE_ID_MIN_VALUE = 1000000;
	private final static int MESSAGE_ID_MAX_VALUE = Integer.MAX_VALUE;

	private final WebSocketSession session;

	@ToString.Exclude
	private final ScheduledFuture<?> pingSchedule;

	@ToString.Exclude
	@Getter(AccessLevel.PRIVATE)
	private final AtomicInteger currentMessageId = new AtomicInteger(MESSAGE_ID_MIN_VALUE - 1);

	public String getNextMessageId() {
		return String.valueOf(
				currentMessageId.updateAndGet(
						current -> current == MESSAGE_ID_MAX_VALUE ? MESSAGE_ID_MIN_VALUE : current + 1));
	}
}
