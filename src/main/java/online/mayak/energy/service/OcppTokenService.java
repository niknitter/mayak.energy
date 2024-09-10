package online.mayak.energy.service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import online.mayak.energy.entity.OcppToken;
import online.mayak.energy.ocpp16.model.types.AuthorizationStatus;
import online.mayak.energy.ocpp16.model.types.IdTagInfo;
import online.mayak.energy.repository.OcppTokenRepository;
import online.mayak.energy.utils.SystemUtils;

@Service
@RequiredArgsConstructor
public class OcppTokenService extends EntityService<OcppToken, OcppTokenRepository, Long> {

	public OcppToken findByIdTag(String idTag) {
		return repository.findByIdTag(idTag)
				.orElseThrow(() -> new EntityNotFoundException());
	}

	public OcppToken findByIdTagOrNull(String idTag) {
		return repository.findByIdTag(idTag)
				.orElse(null);
	}

	/**
	 * TODO добавить проверку на AuthorizationStatus.ConcurrentTx
	 */
	public IdTagInfo getIdTagInfo(String idTag) {
		IdTagInfo idTagInfo;
		OcppToken ocppTag = findByIdTagOrNull(idTag);
		if (ocppTag == null) {
			idTagInfo = IdTagInfo.builder()
					.status(AuthorizationStatus.Invalid)
					.build();
		} else {
			AuthorizationStatus authorizationStatus;
			if (ocppTag.getBlocked() != null)
				authorizationStatus = AuthorizationStatus.Blocked;
			else if (ocppTag.getExpired() != null && ocppTag.getExpired().isBefore(SystemUtils.currentLocalDateTime()))
				authorizationStatus = AuthorizationStatus.Expired;
			else
				authorizationStatus = AuthorizationStatus.Accepted;

			idTagInfo = IdTagInfo.builder()
					.expiryDate(ocppTag.getExpired() != null ? SystemUtils.localToZonedUtcDateTime(ocppTag.getExpired()) : null)
					.parentIdTag(ocppTag.getParent() != null ? ocppTag.getParent().getIdTag() : null)
					.status(authorizationStatus)
					.build();
		}
		return idTagInfo;
	}
}
