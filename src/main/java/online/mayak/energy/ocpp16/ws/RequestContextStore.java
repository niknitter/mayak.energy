package online.mayak.energy.ocpp16.ws;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mayak.energy.ocpp16.Action;
import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.utils.LockByKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestContextStore {

	private final LockByKey<String> lock = new LockByKey<>();
	private final ConcurrentHashMap<String, List<RequestContext>> store = new ConcurrentHashMap<>();

	public void add(String chargePointId, String messageId, Action action, ActionRequest actionRequest, 
			final CompletableFuture<ActionResponse> actionResponseFuture) {
		lock.lock(chargePointId);
		try {
			List<RequestContext> contexts = store.computeIfAbsent(chargePointId, (s) -> new ArrayList<RequestContext>());
			RequestContext context = new RequestContext(messageId, action, actionRequest, actionResponseFuture);
			contexts.add(context);
			log.debug("RequestContext [chargePointId: {}] added to store: {}", chargePointId, context);
		} finally {
			lock.unlock(chargePointId);
		}
	}

	public void remove(String chargePointId, String messageId) {
		lock.lock(chargePointId);
		try {
			List<RequestContext> contexts = store.get(chargePointId);
			if(contexts == null)
				return;
			Iterator<RequestContext> i = contexts.iterator();
			while(i.hasNext()) {
				RequestContext context = i.next();
				if(context.getMessageId().equals(messageId)) {
					// Удалить контекст через итератор
					i.remove();
					log.debug("RequestContext [chargePointId: {}] removed from store: {}", chargePointId, context);
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

	public void remove(String chargePointId) {
		lock.lock(chargePointId);
		try {
			// Удалить из хранилища ChargePoint
			if(store.remove(chargePointId) != null)
				log.debug("RequestContext [chargePointId: {}] ALL removed from store", chargePointId);
		} finally {
			lock.unlock(chargePointId);
		}
	}

	public RequestContext get(String chargePointId, String messageId) {
		lock.lock(chargePointId);
		try {
			List<RequestContext> contexts = store.get(chargePointId);
			if(contexts == null)
				throw new RuntimeException(String.format("RequestContext [chargePointId: %s] not found in store", chargePointId));
			return contexts.stream()
					.filter(f -> f.getMessageId().equals(messageId))
					.findFirst()
					.orElseThrow(() -> new RuntimeException(String.format("RequestContext [chargePointId: %s] not found for messageId: %s", chargePointId, messageId)));
		} finally {
			lock.unlock(chargePointId);
		}
	}

}
