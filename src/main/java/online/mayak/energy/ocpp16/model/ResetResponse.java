package online.mayak.energy.ocpp16.model;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.ResetStatus;

@Getter
@Setter
public class ResetResponse implements ActionResponse {
	private ResetStatus status;
}
