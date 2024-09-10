package online.mayak.energy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import online.mayak.energy.entity.ConnectorStatus;

public interface ConnectorStatusRepository extends JpaRepository<ConnectorStatus, Long> {
	
}
