package online.mayak.energy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AdviceController {

	@Value("${spring.application.name}")
	private String aplicationName;

	@ModelAttribute("applicationName")
	public String applicationName() {
		return aplicationName;
	}

}
