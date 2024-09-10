package online.mayak.energy.ocpp16.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.config.OcppConfig;

@Getter
@Setter
@Builder
public class ReserveNowRequest implements ActionRequest {
	private Integer connectorId;
	@JsonFormat(pattern = OcppConfig.DATE_TIME_FORMAT)
	private ZonedDateTime expiryDate;
	private String idTag;
	private String parentIdTag;
	private Integer reservationId;
}
