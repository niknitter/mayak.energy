package online.mayak.energy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import online.mayak.energy.entity.ConnectorValue;

public interface ConnectorValueRepository extends JpaRepository<ConnectorValue, Long> {

}
