package online.mayak.energy.ocpp16.model;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.ReservationStatus;

@Getter
@Setter
public class ReserveNowResponse implements ActionResponse {
	private ReservationStatus status;
}
