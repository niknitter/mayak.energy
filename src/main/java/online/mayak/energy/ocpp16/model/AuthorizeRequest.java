package online.mayak.energy.ocpp16.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizeRequest implements ActionRequest {
	private String idTag;
}
