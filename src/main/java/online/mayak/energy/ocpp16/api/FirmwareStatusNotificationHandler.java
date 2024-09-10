package online.mayak.energy.ocpp16.api;

import org.springframework.stereotype.Component;

import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.ocpp16.model.FirmwareStatusNotificationResponse;

@Component
public class FirmwareStatusNotificationHandler implements ActionHandler {

	@Override
	public ActionResponse handleRequest(String chargePointId, ActionRequest actionRequest) {
		//FirmwareStatusNotificationRequest request = (FirmwareStatusNotificationRequest) actionRequest;
		return FirmwareStatusNotificationResponse.builder()
				.build();
	}

}
