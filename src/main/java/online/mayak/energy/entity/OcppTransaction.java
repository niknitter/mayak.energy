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

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OcppTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime created;

	@ManyToOne
	@JoinColumn(name = "connector_id")
	private Connector connector;

	@ManyToOne
	@JoinColumn(name = "token_id")
	private OcppToken token;

	private Instant startUtc;

	private Integer startValue;

	private Instant stopUtc;

	private Integer stopValue;

	private String stopReason;

}
