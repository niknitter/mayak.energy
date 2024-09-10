package online.mayak.energy.ocpp16.model.types;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SampledValue {
	private String value;
	private ReadingContext context;
	private ValueFormat format;
	private Measurand measurand;
	private Phase phase;
	private Location location;
	private UnitOfMeasure unit;
}
