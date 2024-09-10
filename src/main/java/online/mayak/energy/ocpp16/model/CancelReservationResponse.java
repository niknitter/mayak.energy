package online.mayak.energy.ocpp16.model;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.CancelReservationStatus;

@Getter
@Setter
public class CancelReservationResponse implements ActionResponse {
	private CancelReservationStatus status;
}
