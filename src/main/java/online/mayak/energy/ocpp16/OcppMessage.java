package online.mayak.energy.ocpp16;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OcppMessage {

	public final static int MESSAGE_TYPE_ID_CALL 		= 2;
	public final static int MESSAGE_TYPE_ID_CALLRESULT 	= 3;
	public final static int MESSAGE_TYPE_ID_CALLERROR 	= 4;

	private final int messageTypeId;
	private final String messageId;
}
