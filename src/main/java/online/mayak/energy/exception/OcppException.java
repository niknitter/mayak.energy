package online.mayak.energy.exception;

import lombok.Getter;

@Getter
public class OcppException extends RuntimeException {

	private static final long serialVersionUID = -6325993175770734339L;

	private OcppErrorCode code = OcppErrorCode.InternalError;

	private String description;

	private Object details;

	public OcppException(String message) {
		super(message);
	}

	public OcppException(String message, Throwable cause) {
		super(message, cause);
	}

	public OcppException(OcppErrorCode code, String description) {
		super(String.format("code: %s; description: %s", code, description));
		this.code = code;
		this.description = description;
	}

	public OcppException(OcppErrorCode code, String description, Object details) {
		super(String.format("code: %s; description: %s; details: %s", code, description, details));
		this.code = code;
		this.description = description;
		this.details = details;
	}

}
