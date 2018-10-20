package com.foreach.across.autoconfigure;

import com.foreach.across.core.context.bootstrap.AcrossBootstrapConfig;
import com.foreach.across.core.context.bootstrap.AcrossBootstrapConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.ObjectMapperConfigurer;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 * Enables Swagger2 in Spring Boot applications.
 * Ensures that the correct ObjectMapper is detected and is published by the {@link ObjectMapperConfigurer} using the ObjectMapperConfigured event.
 *
 * @author Steven Gentens
 * @since 1.0.0
 */
@Configuration
@ConditionalOnBean(Swagger2DocumentationConfiguration.class)
@RequiredArgsConstructor
public class Swagger2AutoConfiguration implements AcrossBootstrapConfigurer
{
	@Override
	public void configureContext( AcrossBootstrapConfig contextConfiguration ) {
		contextConfiguration.extendModule( CONTEXT_POSTPROCESSOR_MODULE, ObjectMapperConfigurer.class );
	}
}

