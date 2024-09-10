package online.mayak.energy.ocpp16.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.IdTagInfo;

@Getter
@Setter
@Builder
public class StartTransactionResponse implements ActionResponse {
	private IdTagInfo idTagInfo;
	private Long transactionId;
}
