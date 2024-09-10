package online.mayak.energy.ocpp16.api;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.ocpp16.Action;

@Component
@RequiredArgsConstructor
public class ActionHandlerFactory {

	private final AuthorizeHandler authorizeHandler;
	private final BootNotificationHandler bootNotificationHandler;
//	private final CancelReservationHandler cancelReservationHandler;
//	private final ClearCacheHandler clearCacheHandler;
	private final DataTransferHandler dataTransferHandler;
	private final DiagnosticsStatusNotificationHandler diagnosticsStatusNotificationHandler;
	private final HeartbeatHandler heartbeatHandler;
	private final MeterValuesHandler meterValuesHandler;
//	private final RemoteStartTransactionHandler remoteStartTransactionHandler;
//	private final RemoteStopTransactionHandler remoteStopTransactionHandler;
//	private final ReserveNowHandler reserveNowHandler;
//	private final ResetHandler resetHandler;
	private final StartTransactionHandler startTransactionHandler;
	private final StatusNotificationHandler statusNotificationHandler;
	private final StopTransactionHandler stopTransactionHandler;

	public ActionHandler getActionHandler(Action action) {
		switch(action) {
		case Authorize:
			return authorizeHandler;
		case BootNotification:
			return bootNotificationHandler;
		case DataTransfer:
			return dataTransferHandler;
		case DiagnosticsStatusNotification:
			return diagnosticsStatusNotificationHandler;
		case Heartbeat:
			return heartbeatHandler;
		case MeterValues:
			return meterValuesHandler;
		case StartTransaction:
			return startTransactionHandler;
		case StatusNotification:
			return statusNotificationHandler;
		case StopTransaction:
			return stopTransactionHandler;
		default:
			return null;
		}
	}

//	public ActionResponseHandler getActionResponseHandler(Action action) {
//		switch(action) {
//		case CancelReservation:
//			return cancelReservationHandler;
//		case ClearCache:
//			return clearCacheHandler;
//		case DataTransfer:
//			return dataTransferHandler;
//		case RemoteStartTransaction:
//			return remoteStartTransactionHandler;
//		case RemoteStopTransaction:
//			return remoteStopTransactionHandler;
//		case ReserveNow:
//			return reserveNowHandler;
//		case Reset:
//			return resetHandler;
//		default:
//			return null;
//		}
//	}

}
