package ax.application.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.format.support.FormattingConversionService;

@Configuration
public class SessionApplicationConfiguration {
	@Autowired
	public void registerConverters(FormattingConversionService mvcConversionService, ConfigurableBeanFactory beanFactory ) {
		mvcConversionService.addConverter(Object.class, byte[].class, new SerializingConverter());
		mvcConversionService.addConverter(byte[].class, Object.class, new DeserializingConverter(beanFactory.getBeanClassLoader()));
	}
}
