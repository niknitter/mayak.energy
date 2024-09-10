package online.mayak.energy.ocpp16.api;

import org.springframework.stereotype.Component;

import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.ocpp16.model.DiagnosticsStatusNotificationResponse;

@Component
public class DiagnosticsStatusNotificationHandler implements ActionHandler {

	@Override
	public ActionResponse handleRequest(String chargePointId, ActionRequest actionRequest) {
		//DiagnosticsStatusNotificationRequest request = (DiagnosticsStatusNotificationRequest) actionRequest;
		return DiagnosticsStatusNotificationResponse.builder()
				.build();
	}

}
