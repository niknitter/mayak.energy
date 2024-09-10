package online.mayak.energy.ocpp16.api;

import java.time.Instant;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.entity.Charger;
import online.mayak.energy.entity.Connector;
import online.mayak.energy.entity.ConnectorStatus;
import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.ocpp16.model.StatusNotificationRequest;
import online.mayak.energy.ocpp16.model.StatusNotificationResponse;
import online.mayak.energy.service.ChargerService;
import online.mayak.energy.service.ConnectorService;
import online.mayak.energy.service.ConnectorStatusService;
import online.mayak.energy.utils.SystemUtils;

@Component
@RequiredArgsConstructor
public class StatusNotificationHandler implements ActionHandler {

	private final ChargerService chargerService;
	private final ConnectorService connectorService;
	private final ConnectorStatusService connectorStatusService;

	@Override
	public ActionResponse handleRequest(String chargePointId, ActionRequest actionRequest) {
		StatusNotificationRequest request = (StatusNotificationRequest) actionRequest;
		Charger charger = chargerService.findByChargePointId(chargePointId);
		Instant statusUtc = request.getTimestamp() != null ? request.getTimestamp().toInstant() : SystemUtils.currentInstant();
		Connector connector = connectorService.findAndUpdateByChargerAndConnectorIdOrCreate(
				charger, 
				request.getConnectorId(), 
				statusUtc, 
				request.getStatus().toString(), 
				request.getErrorCode().toString());
		ConnectorStatus connectorStatus = ConnectorStatus.builder()
				.created(SystemUtils.currentLocalDateTime())
				.connector(connector)
				.statusUtc(statusUtc)
				.status(request.getStatus().toString())
				.errorCode(request.getErrorCode().toString())
				.vendorId(request.getVendorId())
				.vendorErrorCode(request.getVendorErrorCode())
				.build();
		connectorStatusService.saveAndFlush(connectorStatus);
		return StatusNotificationResponse.builder()
				.build();
	}

}
