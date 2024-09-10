package online.mayak.energy.ocpp16.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.ResetType;

@Getter
@Setter
@Builder
public class ResetRequest implements ActionRequest {
	private ResetType type;
}
