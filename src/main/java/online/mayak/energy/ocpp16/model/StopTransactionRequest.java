package online.mayak.energy.ocpp16.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.config.OcppConfig;
import online.mayak.energy.ocpp16.model.types.MeterValue;
import online.mayak.energy.ocpp16.model.types.Reason;

@Getter
@Setter
public class StopTransactionRequest implements ActionRequest {
	private String idTag;
	private Integer meterStop;
	@JsonFormat(pattern = OcppConfig.DATE_TIME_FORMAT)
	private ZonedDateTime timestamp;
	private Long transactionId;
	private Reason reason;
	private MeterValue transactionData;
}
