package online.mayak.energy.ocpp16.model;

import lombok.Getter;
import lombok.Setter;
import online.mayak.energy.ocpp16.model.types.ClearCacheStatus;

@Getter
@Setter
public class ClearCacheResponse implements ActionResponse {
	private ClearCacheStatus status;
}
