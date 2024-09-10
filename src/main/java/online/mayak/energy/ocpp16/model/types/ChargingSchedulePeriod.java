package online.mayak.energy.ocpp16.model.types;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChargingSchedulePeriod {
	private Integer startPeriod;
	private Double limit;
	private Integer numberPhases;
}
