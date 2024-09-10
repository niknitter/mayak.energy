package online.mayak.energy.ocpp16.api;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.entity.OcppTransaction;
import online.mayak.energy.exception.OcppErrorCode;
import online.mayak.energy.exception.OcppException;
import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.ocpp16.model.StopTransactionRequest;
import online.mayak.energy.ocpp16.model.StopTransactionResponse;
import online.mayak.energy.ocpp16.model.types.IdTagInfo;
import online.mayak.energy.ocpp16.model.types.Reason;
import online.mayak.energy.service.OcppTokenService;
import online.mayak.energy.service.OcppTransactionService;

@Component
@RequiredArgsConstructor
public class StopTransactionHandler implements ActionHandler {

	private final OcppTokenService ocppTokenService;
	private final OcppTransactionService ocppTransactionService;

	/**
	 * Если на StartTransaction сервер вернул status == ConcurrentTx, то на эмуляторе MicroOcpp наблюдается такая ситуация:
	 * 1. Запрос на старт транзации, ответ со статусом ConcurrentTx
	 * Message received: [2,"1000018","StartTransaction",{"connectorId":2,"idTag":"1234567890","meterStart":0,"timestamp":"2024-07-17T09:44:55.401Z"}]
	 *  Message sending: [3,"1000018",{"idTagInfo":{"status":"ConcurrentTx"}}]
	 * 2. Запрос со статусом коннектора Charging - не совсем понятно, почему так, возможно особенность реализации MicroOcpp
	 * Message received: [2,"1000019","StatusNotification",{"connectorId":2,"errorCode":"NoError","status":"Charging","timestamp":"2024-07-17T09:44:55.502Z"}]
	 *  Message sending: [3,"1000019",{}]
	 * 3. Запрос на остановку транзакции с инвалидным transactionId == -1 и причиной "reason":"DeAuthorized"
	 * Message received: [2,"1000020","StopTransaction",{"meterStop":0,"timestamp":"2024-07-17T09:44:55.518Z","transactionId":-1,"reason":"DeAuthorized"}] - запрос на остановку транзакции с reason
	 * Message sending: [3,"1000125",{}]
	 * Message received: [2,"1000126","StatusNotification",{"connectorId":2,"errorCode":"NoError","status":"Finishing","timestamp":"2024-07-17T10:34:01.401Z"}]
	 * Message sending: [3,"1000126",{}]
	 * Возможно в этом случае можно просто проигнорировать такую ситуацию и отправить StopTransactionResponse без idTagInfo.
	 * Тогда коннектор переходит в статус Finishing
	 */
	@Override
	public ActionResponse handleRequest(String chargePointId, ActionRequest actionRequest) {
		StopTransactionRequest request = (StopTransactionRequest) actionRequest;
		OcppTransaction ocppTransaction = ocppTransactionService.findByIdOrNull(request.getTransactionId());
		if(ocppTransaction != null) {
			ocppTransaction.setStopUtc(request.getTimestamp().toInstant());
			ocppTransaction.setStopValue(request.getMeterStop());
			ocppTransaction.setStopReason(request.getReason().toString());
			ocppTransactionService.saveAndFlush(ocppTransaction);
			IdTagInfo idTagInfo = request.getIdTag() != null ? ocppTokenService.getIdTagInfo(request.getIdTag()) : null;
			return StopTransactionResponse.builder()
					.idTagInfo(idTagInfo)
					.build();
		} else {
			if(Reason.DeAuthorized.equals(request.getReason()))
				return StopTransactionResponse.builder()
						.build();
			throw new OcppException(OcppErrorCode.GenericError, String.format("TransactionId not found: %s", request.getTransactionId()));
		}
	}

}
