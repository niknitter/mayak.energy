package online.mayak.energy.ocpp16.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.config.OcppConfig;
import online.mayak.energy.ocpp16.model.types.ChargePointErrorCode;
import online.mayak.energy.ocpp16.model.types.ChargePointStatus;

@Getter
@Setter
public class StatusNotificationRequest implements ActionRequest {
	private Integer connectorId;
	private ChargePointErrorCode errorCode;
	private String info;
	private ChargePointStatus status;
	@JsonFormat(pattern = OcppConfig.DATE_TIME_FORMAT)
	private ZonedDateTime timestamp;
	private String vendorId;
	private String vendorErrorCode;
}
