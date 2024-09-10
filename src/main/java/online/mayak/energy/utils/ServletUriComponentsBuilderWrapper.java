package online.mayak.energy.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class ServletUriComponentsBuilderWrapper {

	public static ServletUriComponentsBuilder fromCurrentRequest() {
		return ServletUriComponentsBuilder.fromCurrentRequest();
	}
}
