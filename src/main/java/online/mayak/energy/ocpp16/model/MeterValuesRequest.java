package online.mayak.energy.ocpp16.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.MeterValue;

@Getter
@Setter
public class MeterValuesRequest implements ActionRequest {
	private Integer connectorId;
	private Long transactionId;
	private List<MeterValue> meterValue;
}
