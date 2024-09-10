package online.mayak.energy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Charger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime created;

	private String chargePointId;

	private String chargePointVendor;

	private String chargePointModel;

	private String chargePointSerialNumber;

	private String chargeBoxSerialNumber;

	private LocalDateTime lastHeartbeatMoment;

}
