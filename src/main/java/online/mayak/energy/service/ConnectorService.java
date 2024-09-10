package online.mayak.energy.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.entity.Charger;
import online.mayak.energy.entity.Connector;
import online.mayak.energy.entity.ICountByGroup;
import online.mayak.energy.repository.ConnectorRepository;
import online.mayak.energy.utils.SystemUtils;

@Service
@RequiredArgsConstructor
public class ConnectorService extends EntityService<Connector, ConnectorRepository, Long> {

	public Connector findByChargerIdAndConnectorIdOrNull(Long chargerId, Integer connectorId) {
		return repository
				.findByChargerIdAndConnectorId(chargerId, connectorId)
				.orElse(null);
	}

	public Connector findByChargerAndConnectorIdOrCreate(Charger charger, Integer connectorId) {
		Connector connector = findByChargerIdAndConnectorIdOrNull(charger.getId(), connectorId);
		if (connector == null) {
			connector = Connector.builder()
					.created(SystemUtils.currentLocalDateTime())
					.charger(charger)
					.connectorId(connectorId)
					.build();
			saveAndFlush(connector);
		}
		return connector;
	}

	public Connector findAndUpdateByChargerAndConnectorIdOrCreate(Charger charger, Integer connectorId, Instant statusUtc, String status, String errorCode) {
		Connector connector = findByChargerIdAndConnectorIdOrNull(charger.getId(), connectorId);
		if (connector == null) {
			connector = Connector.builder()
					.created(SystemUtils.currentLocalDateTime())
					.charger(charger)
					.connectorId(connectorId)
					.statusUtc(statusUtc)
					.status(status)
					.errorCode(errorCode)
					.build();
		} else {
			connector.setStatusUtc(statusUtc);
			connector.setStatus(status);
			connector.setErrorCode(errorCode);
		}
		return saveAndFlush(connector);
	}

	public List<ICountByGroup> getCountByStatus() {
		return repository.countByStatus();
	}
}
