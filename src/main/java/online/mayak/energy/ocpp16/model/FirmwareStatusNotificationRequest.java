package online.mayak.energy.ocpp16.model;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.FirmwareStatus;

@Getter
@Setter
public class FirmwareStatusNotificationRequest implements ActionRequest {
	private FirmwareStatus status;
}
