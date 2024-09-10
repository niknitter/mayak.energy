package online.mayak.energy.ocpp16.model;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.DiagnosticsStatus;

@Getter
@Setter
public class DiagnosticsStatusNotificationRequest implements ActionRequest {
	private DiagnosticsStatus status;
}
