package online.mayak.energy.ocpp16.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.DataTransferStatus;

@Getter
@Setter
@Builder
public class DataTransferResponse implements ActionResponse {
	private DataTransferStatus status;
	private String data;
}
