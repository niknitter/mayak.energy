package online.mayak.energy.ocpp16.model.types;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.config.OcppConfig;

@Getter
@Setter
public class MeterValue {
	@JsonFormat(pattern = OcppConfig.DATE_TIME_FORMAT)
	private ZonedDateTime timestamp;
	private List<SampledValue> sampledValue;
}
