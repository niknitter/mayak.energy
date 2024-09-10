package online.mayak.energy.ocpp16.api;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.entity.Charger;
import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.ocpp16.model.HeartbeatResponse;
import online.mayak.energy.service.ChargerService;
import online.mayak.energy.utils.SystemUtils;

@Component
@RequiredArgsConstructor
public class HeartbeatHandler implements ActionHandler {

	private final ChargerService chargerService;

	@Override
	public ActionResponse handleRequest(String chargePointId, ActionRequest actionRequest) {
		//HeartbeatRequest request = (HeartbeatRequest) actionRequest;
		Charger charger = chargerService.findByChargePointId(chargePointId);
		charger.setLastHeartbeatMoment(SystemUtils.currentLocalDateTime());
		charger = chargerService.saveAndFlush(charger);
		return HeartbeatResponse.builder()
				.currentTime(SystemUtils.currentZonedDateTimeUTC())
				.build();
	}

}
