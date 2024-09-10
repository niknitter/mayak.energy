package online.mayak.energy.utils;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class LocaleMessageSource extends ResourceBundleMessageSource {

	public String getMessage(String code) {
        return getMessage(code, new Object[]{}, code, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object... params) {
        return getMessage(code, params, code, LocaleContextHolder.getLocale());
    }

}
