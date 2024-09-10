package online.mayak.energy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import online.mayak.energy.entity.Connector;
import online.mayak.energy.entity.ICountByGroup;

public interface ConnectorRepository extends JpaRepository<Connector, Long> {

	Optional<Connector> findByChargerIdAndConnectorId(Long chargerId, Integer connectorId);

	@Query(value = ""
			+ "select c.status as group, count(*) as count"
			+ "  from connector as c"
			+ " where c.connector_id > 0"
			+ " group by c.status"
			+ " order by c.status", nativeQuery = true)
	List<ICountByGroup> countByStatus();
}
