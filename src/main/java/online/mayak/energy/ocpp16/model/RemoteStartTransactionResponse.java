package online.mayak.energy.ocpp16.model;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.RemoteStartStopStatus;

@Getter
@Setter
public class RemoteStartTransactionResponse implements ActionResponse {
	private RemoteStartStopStatus status;
}
