package online.mayak.energy.ocpp16.ws;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mayak.energy.config.OcppConfig;
import online.mayak.energy.utils.LockByKey;

/**
 * TODO https://www.saiflow.com/blog/how-mishandling-of-websockets-can-cause-dos-and-energy-theft/
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SessionContextStore {

	private final LockByKey<String> lock = new LockByKey<>();
	private final ConcurrentHashMap<String, List<SessionContext>> store = new ConcurrentHashMap<>();

	private final ScheduledExecutorService scheduledExecutorService;

	public void add(String chargePointId, WebSocketSession session) {
		lock.lock(chargePointId);
		try {
			List<SessionContext> contexts = store.computeIfAbsent(chargePointId, (s) -> new ArrayList<SessionContext>());
			ScheduledFuture<?> pingSchedule = scheduledExecutorService.scheduleAtFixedRate(
					new WebSocketPingTask(session),
					OcppConfig.PING_INTERVAL, 
					OcppConfig.PING_INTERVAL, 
					OcppConfig.PING_INTERVAL_TIME_UNIT);
			SessionContext context = new SessionContext(session, pingSchedule);
			contexts.add(context);
			log.debug("SessionContext [chargePointId: {}] added to store: {}", chargePointId, context);
		} finally {
			lock.unlock(chargePointId);
		}
	}

	public void remove(String chargePointId, WebSocketSession session) {
		lock.lock(chargePointId);
		try {
			List<SessionContext> contexts = store.get(chargePointId);
			if(contexts == null)
				return;
			Iterator<SessionContext> i = contexts.iterator();
			while(i.hasNext()) {
				SessionContext context = i.next();
				if(context.getSession().getId().equals(session.getId())) {
					// Остановить ping-задание
					context.getPingSchedule().cancel(true);
					// Удалить контекст через итератор
					i.remove();
					log.debug("SessionContext [chargePointId: {}] removed from store: {}", chargePointId, context);
					break;
				}
			}
			// Если список контекстов для ChargePoint пустой, то удалить из хранилища
			if(contexts.size() == 0)
				store.remove(chargePointId);
		} finally {
			lock.unlock(chargePointId);
		}
	}

	public SessionContext get(String chargePointId) {
		lock.lock(chargePointId);
		try {
			List<SessionContext> contexts = store.get(chargePointId);
			if(contexts == null || contexts.isEmpty())
				throw new RuntimeException(String.format("SessionContext [chargePointId: %s] not found in store", chargePointId));
			return contexts.get(contexts.size() - 1); // см. TODO 
		} finally {
			lock.unlock(chargePointId);
		}
	}

	public int getCount() {
		return store.size();
	}

	@RequiredArgsConstructor
	public class WebSocketPingTask implements Runnable {

		private static final PingMessage PING_MESSAGE = new PingMessage(ByteBuffer.wrap("ping".getBytes(UTF_8)));

		private final WebSocketSession session;

		@Override
		public void run() {
			OcppHelper.logPingSending(session);
			try {
				session.sendMessage(PING_MESSAGE);
			} catch (IOException e) {
				OcppHelper.logPingError(session, e);
			}
		}
	}

}
