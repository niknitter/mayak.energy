package online.mayak.energy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OcppToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime created;

	private String idTag;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private OcppToken parent;

	private int maxActiveTxCount;

	private LocalDateTime expired;

	private LocalDateTime blocked;

}
