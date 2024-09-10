package online.mayak.energy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import online.mayak.energy.entity.Charger;

public interface ChargerRepository extends JpaRepository<Charger, Long> {

	Optional<Charger> findByChargePointId(String chargePointId);

}
