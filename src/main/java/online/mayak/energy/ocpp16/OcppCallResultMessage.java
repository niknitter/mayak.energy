package online.mayak.energy.ocpp16;

import lombok.Builder;
import lombok.Getter;
import online.mayak.energy.ocpp16.model.ActionResponse;

@Getter
public class OcppCallResultMessage extends OcppMessage {

	private final ActionResponse payload;

	@Builder
	public OcppCallResultMessage(String messageId, ActionResponse payload) {
		super(OcppMessage.MESSAGE_TYPE_ID_CALLRESULT, messageId);
		this.payload = payload;
	}

}
