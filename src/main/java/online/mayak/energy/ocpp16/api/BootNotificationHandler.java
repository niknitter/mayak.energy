package online.mayak.energy.ocpp16.api;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.entity.Charger;
import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.ocpp16.model.BootNotificationRequest;
import online.mayak.energy.ocpp16.model.BootNotificationResponse;
import online.mayak.energy.ocpp16.model.types.RegistrationStatus;
import online.mayak.energy.service.ChargerService;
import online.mayak.energy.utils.SystemUtils;

@Component
@RequiredArgsConstructor
public class BootNotificationHandler implements ActionHandler {

	private final ChargerService chargerService;

	@Override
	public ActionResponse handleRequest(String chargePointId, ActionRequest actionRequest) {
		BootNotificationRequest request = (BootNotificationRequest) actionRequest;

		Charger charger = chargerService.findByChargePointId(chargePointId);
		charger.setChargePointVendor(request.getChargePointVendor());
		charger.setChargePointModel(request.getChargePointModel());
		charger.setChargePointSerialNumber(request.getChargePointSerialNumber());
		charger.setChargeBoxSerialNumber(request.getChargeBoxSerialNumber());
		chargerService.saveAndFlush(charger);

		return BootNotificationResponse.builder()
				.status(RegistrationStatus.Accepted) // TODO Высянить от чего зависит статус
				.currentTime(SystemUtils.currentZonedDateTimeUTC())
				.interval(60)
				.build();
	}

}
