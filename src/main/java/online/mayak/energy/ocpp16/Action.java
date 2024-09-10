package online.mayak.energy.ocpp16;

import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.ocpp16.model.AuthorizeRequest;
import online.mayak.energy.ocpp16.model.AuthorizeResponse;
import online.mayak.energy.ocpp16.model.BootNotificationRequest;
import online.mayak.energy.ocpp16.model.BootNotificationResponse;
import online.mayak.energy.ocpp16.model.CancelReservationRequest;
import online.mayak.energy.ocpp16.model.CancelReservationResponse;
import online.mayak.energy.ocpp16.model.ClearCacheRequest;
import online.mayak.energy.ocpp16.model.ClearCacheResponse;
import online.mayak.energy.ocpp16.model.DataTransferRequest;
import online.mayak.energy.ocpp16.model.DataTransferResponse;
import online.mayak.energy.ocpp16.model.DiagnosticsStatusNotificationRequest;
import online.mayak.energy.ocpp16.model.DiagnosticsStatusNotificationResponse;
import online.mayak.energy.ocpp16.model.FirmwareStatusNotificationRequest;
import online.mayak.energy.ocpp16.model.FirmwareStatusNotificationResponse;
import online.mayak.energy.ocpp16.model.HeartbeatRequest;
import online.mayak.energy.ocpp16.model.HeartbeatResponse;
import online.mayak.energy.ocpp16.model.MeterValuesRequest;
import online.mayak.energy.ocpp16.model.MeterValuesResponse;
import online.mayak.energy.ocpp16.model.RemoteStartTransactionRequest;
import online.mayak.energy.ocpp16.model.RemoteStartTransactionResponse;
import online.mayak.energy.ocpp16.model.RemoteStopTransactionRequest;
import online.mayak.energy.ocpp16.model.RemoteStopTransactionResponse;
import online.mayak.energy.ocpp16.model.ReserveNowRequest;
import online.mayak.energy.ocpp16.model.ReserveNowResponse;
import online.mayak.energy.ocpp16.model.ResetRequest;
import online.mayak.energy.ocpp16.model.ResetResponse;
import online.mayak.energy.ocpp16.model.StartTransactionRequest;
import online.mayak.energy.ocpp16.model.StartTransactionResponse;
import online.mayak.energy.ocpp16.model.StatusNotificationRequest;
import online.mayak.energy.ocpp16.model.StatusNotificationResponse;
import online.mayak.energy.ocpp16.model.StopTransactionRequest;
import online.mayak.energy.ocpp16.model.StopTransactionResponse;

public enum Action {

	Authorize(AuthorizeRequest.class, AuthorizeResponse.class),
	BootNotification(BootNotificationRequest.class, BootNotificationResponse.class),
	CancelReservation(CancelReservationRequest.class, CancelReservationResponse.class),
	//ChangeAvailability(Request.class, Response.class),
	//ChangeConfiguration(Request.class, Response.class),
	ClearCache(ClearCacheRequest.class, ClearCacheResponse.class),
	//ClearChargingProfile(Request.class, Response.class),
	DataTransfer(DataTransferRequest.class, DataTransferResponse.class),
	DiagnosticsStatusNotification(DiagnosticsStatusNotificationRequest.class, DiagnosticsStatusNotificationResponse.class),
	FirmwareStatusNotification(FirmwareStatusNotificationRequest.class, FirmwareStatusNotificationResponse.class),
	//GetCompositeSchedule(Request.class, Response.class),
	//GetConfiguration(Request.class, Response.class),
	//GetDiagnostics(Request.class, Response.class),
	//GetLocalListVersion(Request.class, Response.class),
	Heartbeat(HeartbeatRequest.class, HeartbeatResponse.class),
	MeterValues(MeterValuesRequest.class, MeterValuesResponse.class),
	RemoteStartTransaction(RemoteStartTransactionRequest.class, RemoteStartTransactionResponse.class),
	RemoteStopTransaction(RemoteStopTransactionRequest.class, RemoteStopTransactionResponse.class),
	ReserveNow(ReserveNowRequest.class, ReserveNowResponse.class),
	Reset(ResetRequest.class, ResetResponse.class),
	//SendLocalList(Request.class, Response.class),
	//SetChargingProfile(Request.class, Response.class),
	StartTransaction(StartTransactionRequest.class, StartTransactionResponse.class),
	StatusNotification(StatusNotificationRequest.class, StatusNotificationResponse.class),
	StopTransaction(StopTransactionRequest.class, StopTransactionResponse.class),
	//TriggerMessage(Request.class, Response.class),
	//UnlockConnector(Request.class, Response.class),
	//UpdateFirmware(Request.class, Response.class)
	;

	private final Class<? extends ActionRequest> requestType;
	private final Class<? extends ActionResponse> responseType;

	private Action(Class<? extends ActionRequest> requestType, Class<? extends ActionResponse> responseType) {
		this.requestType = requestType;
		this.responseType = responseType;
	}

	public Class<? extends ActionRequest> requestType() {
		return requestType;
	}

	public Class<? extends ActionResponse> responseType() {
		return responseType;
	}

	public static Action valueOfOrNull(String value) {
		try {
			return valueOf(value);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
