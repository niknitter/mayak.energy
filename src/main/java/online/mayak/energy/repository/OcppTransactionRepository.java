package online.mayak.energy.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import online.mayak.energy.entity.OcppTransaction;

public interface OcppTransactionRepository extends JpaRepository<OcppTransaction, Long> {

	Optional<OcppTransaction> findByConnectorIdAndTokenIdAndStartUtcAndStartValue(Long connectorId, Long tokenId, Instant startUtc, Integer startValue);

	int countByStopUtcIsNullAndStopValueIsNull();

	int countByStopUtcIsNullAndStopValueIsNullAndTokenId(Long tokenId);

}
