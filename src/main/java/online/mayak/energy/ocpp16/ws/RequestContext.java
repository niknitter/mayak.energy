package online.mayak.energy.ocpp16.ws;

import java.util.concurrent.CompletableFuture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import online.mayak.energy.ocpp16.Action;
import online.mayak.energy.ocpp16.model.ActionRequest;
import online.mayak.energy.ocpp16.model.ActionResponse;

@Getter
@ToString
@RequiredArgsConstructor
public class RequestContext {
	private final String messageId;
	private final Action action;
	@ToString.Exclude
	private final ActionRequest actionRequest;
	@ToString.Exclude
	private final CompletableFuture<ActionResponse> futureActionResponse;
}
