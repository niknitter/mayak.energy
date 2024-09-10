package online.mayak.energy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import online.mayak.energy.entity.OcppToken;

public interface OcppTokenRepository extends JpaRepository<OcppToken, Long> {

	Optional<OcppToken> findByIdTag(String idTag);
}
