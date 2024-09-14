package online.mayak.energy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "ocpp")
public class OcppProperties {
	private Boolean autoRegisterNewChargePoint;
}
