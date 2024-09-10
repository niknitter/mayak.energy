package online.mayak.energy.ocpp16.model.types;

public enum ChargePointStatus {
	Available,
	Preparing,
	Charging,
	SuspendedEVSE,
	SuspendedEV,
	Finishing,
	Reserved,
	Unavailable,
	Faulted;
}
