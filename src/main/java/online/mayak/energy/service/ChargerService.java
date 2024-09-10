package online.mayak.energy.service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import online.mayak.energy.entity.Charger;
import online.mayak.energy.ocpp16.ws.SessionContextStore;
import online.mayak.energy.repository.ChargerRepository;

@Service
@RequiredArgsConstructor
public class ChargerService extends EntityService<Charger, ChargerRepository, Long>{

	private final SessionContextStore sessionContextStore;

	public Charger findByChargePointId(String chargePointId) {
		return repository.findByChargePointId(chargePointId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Charger not found by chargePointId: %s", chargePointId)));
	}

	public Charger findByChargePointIdOrNull(String chargePointId) {
		return repository.findByChargePointId(chargePointId)
				.orElse(null);
	}

	public int getOnlineCount() {
		return sessionContextStore.getCount();
	}
}
