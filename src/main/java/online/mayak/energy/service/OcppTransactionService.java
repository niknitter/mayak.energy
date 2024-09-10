package online.mayak.energy.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.entity.Connector;
import online.mayak.energy.entity.OcppToken;
import online.mayak.energy.entity.OcppTransaction;
import online.mayak.energy.repository.OcppTransactionRepository;
import online.mayak.energy.utils.SystemUtils;

@Service
@RequiredArgsConstructor
public class OcppTransactionService extends EntityService<OcppTransaction, OcppTransactionRepository, Long> {

	public OcppTransaction findByConnectorIdAndTokenIdAndStartUtcAndStartValueOrNull(Long connectorId, Long tokenId, Instant startUtc,
			Integer startValue) {
		return repository.findByConnectorIdAndTokenIdAndStartUtcAndStartValue(connectorId, tokenId, startUtc, startValue)
				.orElse(null);
	}

	public OcppTransaction findByConnectorAndTokenAndStartUtcAndStartValueOrCreate(Connector connector, OcppToken token, Instant startUtc,
			Integer startValue) {
		OcppTransaction ocppTransaction = findByConnectorIdAndTokenIdAndStartUtcAndStartValueOrNull(connector.getId(), token.getId(), startUtc, startValue);
		if (ocppTransaction == null) {
			ocppTransaction = OcppTransaction.builder()
					.created(SystemUtils.currentLocalDateTime())
					.connector(connector)
					.token(token)
					.startUtc(startUtc)
					.startValue(startValue)
					.build();
			saveAndFlush(ocppTransaction);
		}
		return ocppTransaction;
	}

	public int getActiveCount() {
		return repository.countByStopUtcIsNullAndStopValueIsNull();
	}

	public int getActiveCountByTokenId(Long tokenId) {
		return repository.countByStopUtcIsNullAndStopValueIsNullAndTokenId(tokenId);
	}

}
