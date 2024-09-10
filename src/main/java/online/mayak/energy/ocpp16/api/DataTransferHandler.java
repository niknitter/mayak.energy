package online.mayak.energy.ocpp16.api;

import org.springframework.stereotype.Component;

import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.ocpp16.model.DataTransferResponse;

@Component
public class DataTransferHandler implements ActionHandler {

	@Override
	public ActionResponse handleRequest(String chargePointId, ActionRequest actionRequest) {
		//DataTransferRequest request = (DataTransferRequest) actionRequest;
		return DataTransferResponse.builder()
				.build();
	}

}
