package online.mayak.energy.ocpp16;

import lombok.Builder;
import lombok.Getter;
import online.mayak.energy.ocpp16.model.ActionRequest;

@Getter
public class OcppCallMessage extends OcppMessage {

	private final Action action;
	private final ActionRequest payload;

	@Builder
	public OcppCallMessage(String messageId, Action action, ActionRequest payload) {
		super(OcppMessage.MESSAGE_TYPE_ID_CALL, messageId);
		this.action = action;
		this.payload = payload;
	}

}
