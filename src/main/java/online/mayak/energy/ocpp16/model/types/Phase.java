package online.mayak.energy.ocpp16.model.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Phase {
	L1("L1"),
	L2("L2"),
	L3("L3"),
	N("N"),
	L1_N("L1-N"),
	L2_N("L2-N"),
	L3_N("L3-N"),
	L1_L2("L1-L2"),
	L2_L3("L2-L3"),
	L3_L1("L3-L1");

	private final String tag;

	Phase(String tag) {
		this.tag = tag;
	}

	@JsonValue
	public String tag() {
		return tag;
	}
}
