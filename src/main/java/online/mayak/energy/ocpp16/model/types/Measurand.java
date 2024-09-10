package online.mayak.energy.ocpp16.model.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Measurand {
	EnergyActiveExportRegister("Energy.Active.Export.Register"),
	EnergyActiveImportRegister("Energy.Active.Import.Register"),
	EnergyReactiveExportRegister("Energy.Reactive.Export.Register"),
	EnergyReactiveImportRegister("Energy.Reactive.Import.Register"),
	EnergyActiveExportInterval("Energy.Active.Export.Interval"),
	EnergyActiveIInterval("Energy.Active.Import.Interval"),
	EnergyReactiveExportInterval("Energy.Reactive.Export.Interval"),
	EnergyReactiveImportInterval("Energy.Reactive.Import.Interval"),
	PowerActiveExport("Power.Active.Export"),
	PowerActiveImport("Power.Active.Import"),
	PowerOffered("Power.Offered"),
	PowerReactiveExport("Power.Reactive.Export"),
	PowerReactiveImport("Power.Reactive.Import"),
	PowerFactor("Power.Factor"),
	CurrentImport("Current.Import"),
	CurrentExport("Current.Export"),
	CurrentOffered("Current.Offered"),
	Voltage("Voltage"),
	Frequency("Frequency"),
	Temperature("Temperature"),
	SoC("SoC"),
	RPM("RPM");

	private final String tag;

	Measurand(String tag) {
		this.tag = tag;
	}

	@JsonValue
	public String tag() {
		return tag;
	}
}
