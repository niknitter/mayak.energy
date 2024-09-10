package online.mayak.energy.ocpp16.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.config.OcppConfig;
import online.mayak.energy.ocpp16.model.types.RegistrationStatus;

@Getter
@Setter
@Builder
public class BootNotificationResponse implements ActionResponse {
	private RegistrationStatus status;
	@JsonFormat(pattern = OcppConfig.DATE_TIME_FORMAT)
	private ZonedDateTime currentTime;
	private Integer interval;
}
