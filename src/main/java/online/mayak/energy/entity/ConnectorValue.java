package online.mayak.energy.entity;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectorValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime created;

	@ManyToOne
	@JoinColumn(name = "connector_id")
	private Connector connector;

	@ManyToOne
	@JoinColumn(name = "transaction_id")
	private OcppTransaction transaction;

	private Instant valueUtc;

	private String value;

	private String context;

	private String format;

	private String measurand;

	private String phase;

	private String location;

	private String unit;

}
