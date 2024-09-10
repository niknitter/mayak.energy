package online.mayak.energy.ocpp16.model.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReadingContext {
	InterruptionBegin("Interruption.Begin"),
	InterruptionEnd("Interruption.End"),
	SampleClock("Sample.Clock"),
	SamplePeriodic("Sample.Periodic"),
	TransactionBegin("Transaction.Begin"),
	TransactionEnd("Transaction.End"),
	Trigger("Trigger"),
	Other("Other");

	private final String tag;

	ReadingContext(String tag) {
		this.tag = tag;
	}

	@JsonValue
	public String tag() {
		return tag;
	}
}
