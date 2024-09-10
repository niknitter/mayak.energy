package online.mayak.energy.ocpp16.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.entity.Charger;
import online.mayak.energy.entity.Connector;
import online.mayak.energy.entity.ConnectorValue;
import online.mayak.energy.entity.OcppTransaction;
import online.mayak.energy.exception.OcppErrorCode;
import online.mayak.energy.exception.OcppException;
import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.ocpp16.model.MeterValuesRequest;
import online.mayak.energy.ocpp16.model.MeterValuesResponse;
import online.mayak.energy.ocpp16.model.types.MeterValue;
import online.mayak.energy.ocpp16.model.types.SampledValue;
import online.mayak.energy.service.ChargerService;
import online.mayak.energy.service.ConnectorService;
import online.mayak.energy.service.ConnectorValueService;
import online.mayak.energy.service.OcppTransactionService;
import online.mayak.energy.utils.SystemUtils;

@Component
@RequiredArgsConstructor
public class MeterValuesHandler implements ActionHandler {

	private final ChargerService chargerService;
	private final ConnectorService connectorService;
	private final OcppTransactionService ocppTransactionService;
	private final ConnectorValueService connectorValueService;

	@Override
	public ActionResponse handleRequest(String chargePointId, ActionRequest actionRequest) {
		MeterValuesRequest request = (MeterValuesRequest) actionRequest;
		Charger charger = chargerService.findByChargePointId(chargePointId);
		Connector connector;
		OcppTransaction ocppTransaction = null;
		// Если передана транзакция, то connetcorId запроса должен совпадать с connectorId коннектора транзакции
		if(request.getTransactionId() != null && request.getTransactionId().longValue() > 0) {
			ocppTransaction = ocppTransactionService.findById(request.getTransactionId());
			if(!request.getConnectorId().equals(ocppTransaction.getConnector().getConnectorId()))
				throw new OcppException(OcppErrorCode.PropertyConstraintViolation, 
						String.format("Incorrect connectorId '%s', ecpected '%s'", request.getConnectorId(), ocppTransaction.getConnector().getConnectorId()));
			connector = ocppTransaction.getConnector();
		} else {
			connector = connectorService.findByChargerAndConnectorIdOrCreate(charger, request.getConnectorId());
		}
		List<ConnectorValue> connectorValues = new ArrayList<>();
		for(MeterValue meterValue : request.getMeterValue())
			for(SampledValue sampledValue : meterValue.getSampledValue())
				connectorValues.add(ConnectorValue.builder()
						.created(SystemUtils.currentLocalDateTime())
						.connector(connector)
						.transaction(ocppTransaction)
						.valueUtc(meterValue.getTimestamp().toInstant())
						.value(sampledValue.getValue())
						.context(sampledValue.getContext() != null ? sampledValue.getContext().toString() : null)
						.format(sampledValue.getFormat() != null ? sampledValue.getFormat().toString() : null)
						.measurand(sampledValue.getMeasurand() != null ? sampledValue.getMeasurand().toString() : null)
						.phase(sampledValue.getPhase() != null ? sampledValue.getPhase().toString() : null)
						.location(sampledValue.getLocation() != null ? sampledValue.getLocation().toString() : null)
						.unit(sampledValue.getUnit() != null ? sampledValue.getUnit().toString() : null)
						.build());

		connectorValueService.saveAllAndFlush(connectorValues);

		return MeterValuesResponse.builder()
				.build();
	}

}
