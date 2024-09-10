package online.mayak.energy.ocpp16.api;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;
import online.mayak.energy.ocpp16.model.AuthorizeRequest;
import online.mayak.energy.ocpp16.model.AuthorizeResponse;
import online.mayak.energy.ocpp16.model.types.IdTagInfo;
import online.mayak.energy.service.OcppTokenService;

@Component
@RequiredArgsConstructor
public class AuthorizeHandler implements ActionHandler {

	private final OcppTokenService tokenService;

	@Override
	public ActionResponse handleRequest(String chargePointId, ActionRequest actionRequest) {
		AuthorizeRequest request = (AuthorizeRequest) actionRequest;
		IdTagInfo idTagInfo = tokenService.getIdTagInfo(request.getIdTag());
		return AuthorizeResponse.builder()
					.idTagInfo(idTagInfo)
					.build();
	}

}
