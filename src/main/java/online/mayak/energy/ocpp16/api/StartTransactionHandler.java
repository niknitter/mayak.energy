package online.mayak.energy.ocpp16.api;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.entity.Charger;
import online.mayak.energy.entity.Connector;
import online.mayak.energy.entity.OcppToken;
import online.mayak.energy.entity.OcppTransaction;
import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.ocpp16.model.StartTransactionRequest;
import online.mayak.energy.ocpp16.model.StartTransactionResponse;
import online.mayak.energy.ocpp16.model.types.AuthorizationStatus;
import online.mayak.energy.ocpp16.model.types.IdTagInfo;
import online.mayak.energy.service.ConnectorService;
import online.mayak.energy.service.ChargerService;
import online.mayak.energy.service.OcppTokenService;
import online.mayak.energy.service.OcppTransactionService;

@Component
@RequiredArgsConstructor
public class StartTransactionHandler implements ActionHandler {

	private final ChargerService chargerService;
	private final OcppTokenService tokenService;
	private final ConnectorService connectorService;
	private final OcppTransactionService transactionService;

	@Override
	public ActionResponse handleRequest(String chargePointId, ActionRequest actionRequest) {
		StartTransactionRequest request = (StartTransactionRequest) actionRequest;
		// Получить
		IdTagInfo idTagInfo = tokenService.getIdTagInfo(request.getIdTag());
		Long transactionId = null;
		if (AuthorizationStatus.Accepted.equals(idTagInfo.getStatus())) {
			Charger charger = chargerService.findByChargePointId(chargePointId);
			Connector connector = connectorService.findByChargerAndConnectorIdOrCreate(charger, request.getConnectorId());
			OcppToken token = tokenService.findByIdTag(request.getIdTag());
			// Проверить ограничение на максимальное количество активных транзакций (0 - ограничений нет)
			if(token.getMaxActiveTxCount() > 0 && transactionService.getActiveCountByTokenId(token.getId()) >= token.getMaxActiveTxCount()) {
				idTagInfo.setStatus(AuthorizationStatus.ConcurrentTx);
			} else {
				OcppTransaction ocppTransaction = transactionService.findByConnectorAndTokenAndStartUtcAndStartValueOrCreate(
						connector,
						token,
						request.getTimestamp().toInstant(),
						request.getMeterStart());
				transactionId = ocppTransaction.getId();
			}
		}
		return StartTransactionResponse.builder()
				.idTagInfo(idTagInfo)
				.transactionId(transactionId)
				.build();
	}

}
