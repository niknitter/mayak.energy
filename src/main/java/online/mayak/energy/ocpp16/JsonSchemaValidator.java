package online.mayak.energy.ocpp16;

import java.io.IOException;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JsonSchemaValidator {

	public Set<ValidationMessage> validateRequest(Action action, JsonNode jsonNode) throws IOException {
		return validate(String.format("json/ocpp16/%s.json", action.toString()), jsonNode);
	}

	public Set<ValidationMessage> validateResponse(Action action, JsonNode jsonNode) throws IOException {
		return validate(String.format("json/ocpp16/%s%s.json", action.toString(), "Response"), jsonNode);
	}

	private Set<ValidationMessage> validate(String schemaPath, JsonNode jsonNode) throws IOException {
		Resource resource = new ClassPathResource(schemaPath);
		JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
		JsonSchema jsonSchema = factory.getSchema(resource.getInputStream());
		return jsonSchema.validate(jsonNode);
	}
}
