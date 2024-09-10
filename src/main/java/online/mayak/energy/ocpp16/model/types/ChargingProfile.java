package online.mayak.energy.ocpp16.model.types;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.config.OcppConfig;

@Getter
@Setter
@Builder
public class ChargingProfile {
	private Integer chargingProfileId;
	private Long transactionId;
	private Integer stackLevel;
	private ChargingProfilePurposeType chargingProfilePurpose;
	private ChargingProfileKindType chargingProfileKind;
	private RecurrencyKindType recurrencyKind;
	@JsonFormat(pattern = OcppConfig.DATE_TIME_FORMAT)
	private ZonedDateTime validFrom;
	@JsonFormat(pattern = OcppConfig.DATE_TIME_FORMAT)
	private ZonedDateTime validTo;
	private ChargingSchedule chargingSchedule;
}
