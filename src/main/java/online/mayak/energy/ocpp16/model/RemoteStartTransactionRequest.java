package online.mayak.energy.ocpp16.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.ChargingProfile;

@Getter
@Setter
@Builder
public class RemoteStartTransactionRequest implements ActionRequest {
	private Integer connectorId;
	private String idTag;
	private ChargingProfile chargingProfile;
}
