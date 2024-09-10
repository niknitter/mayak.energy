package online.mayak.energy.ocpp16;

import lombok.Builder;
import lombok.Getter;
import online.mayak.energy.exception.OcppErrorCode;

@Getter
public class OcppCallErrorMessage extends OcppMessage {

	private final OcppErrorCode errorCode;
	private final String errorDescription;
	private final Object errorDetails;

	@Builder
	public OcppCallErrorMessage(String messageId, OcppErrorCode errorCode, String errorDescription, Object errorDetails) {
		super(OcppMessage.MESSAGE_TYPE_ID_CALLERROR, messageId);
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
		this.errorDetails = errorDetails;
	}

}
