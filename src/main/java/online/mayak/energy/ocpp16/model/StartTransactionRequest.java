package online.mayak.energy.ocpp16.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.config.OcppConfig;

@Getter
@Setter
public class StartTransactionRequest implements ActionRequest {
	private Integer connectorId;
    private String idTag;
    private Integer meterStart;
    private Integer reservationId;
	@JsonFormat(pattern = OcppConfig.DATE_TIME_FORMAT)
    private ZonedDateTime timestamp;
}
