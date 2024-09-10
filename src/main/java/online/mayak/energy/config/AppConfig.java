package online.mayak.energy.config;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import online.mayak.energy.utils.LocaleMessageSource;

@Slf4j
@Configuration
@EnableScheduling
@EnableTransactionManagement
public class AppConfig {

	public final static String API_V1_PATH = "/api/v1"; 

	private ScheduledExecutorService scheduledExecutorService;

    @Bean
    static LocaleMessageSource localeMessageSource() {
        LocaleMessageSource messageSource = new LocaleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasename("messages");
        return messageSource;
    }

    @Bean
    static LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(localeMessageSource());
        return bean;
    }

    @Bean
    static MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor bean = new MethodValidationPostProcessor();
        bean.setValidator(getValidator());
        return bean; 
    }

	/**
	 * Для работы с Java 8 date/time types необходимо подключить модуль jackson-datatype-jsr310
	 * https://www.baeldung.com/java-jackson-offsetdatetime
	 */
	@Bean
	ObjectMapper objectMapper() {
		return JsonMapper.builder()
				.addModule(new JavaTimeModule())
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.serializationInclusion(Include.NON_NULL)
				.build();
	}

	@Bean
	ScheduledExecutorService scheduledExecutorService() {
		scheduledExecutorService = new ScheduledThreadPoolExecutor(5);
		return scheduledExecutorService;
	}

	@PreDestroy
	public void preDestroy() {
		if(scheduledExecutorService != null)
			try {
				log.info("ScheduledExecutorService shutdowning...");
				scheduledExecutorService.shutdown();
				scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				scheduledExecutorService.shutdownNow();
				log.info("ScheduledExecutorService shutdowned");
			}
	}
}
