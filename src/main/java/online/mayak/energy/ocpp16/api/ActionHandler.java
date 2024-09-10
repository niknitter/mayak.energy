package online.mayak.energy.ocpp16.api;

import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;

public interface ActionHandler {

	public abstract ActionResponse handleRequest(String chargePointId, ActionRequest actionRequest);

}
