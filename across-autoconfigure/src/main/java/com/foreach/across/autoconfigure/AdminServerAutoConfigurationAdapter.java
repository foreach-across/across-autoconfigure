package com.foreach.across.autoconfigure;

import de.codecentric.boot.admin.server.config.AdminServerAutoConfiguration;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.accept.ContentNegotiationManager;

/**
 * Modifies the <strong>adminHandlerMapping</strong> to detect {@link de.codecentric.boot.admin.server.web.AdminController}
 * from ancestor contexts as well.
 *
 * @author Arne Vandamme
 * @since 2.0.0
 */
@Configuration
@Import( { WebClientAutoConfiguration.class, AdminServerAutoConfiguration.class } )
public class AdminServerAutoConfigurationAdapter implements BeanDefinitionRegistryPostProcessor
{
	@Override
	public void postProcessBeanDefinitionRegistry( BeanDefinitionRegistry registry ) throws BeansException {
		// removes the original adminHandlerMapping - we only need one
		if ( registry.containsBeanDefinition( "adminHandlerMapping" ) ) {
			registry.removeBeanDefinition( "adminHandlerMapping" );
		}
	}

	@Override
	public void postProcessBeanFactory( ConfigurableListableBeanFactory beanFactory ) throws BeansException {
	}

	@Configuration
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	public static class ServletRestApiConfiguration
	{
		private final AdminServerProperties adminServerProperties;

		public ServletRestApiConfiguration( AdminServerProperties adminServerProperties ) {
			this.adminServerProperties = adminServerProperties;
		}

		@Bean
		public org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping exposedAwareAdminHandlerMapping( ContentNegotiationManager contentNegotiationManager ) {
			org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping mapping =
					new de.codecentric.boot.admin.server.web.servlet.AdminControllerHandlerMapping(
							adminServerProperties.getContextPath() );
			// put the order one lower than the original, in case the original did not get removed ours should still take precedence
			mapping.setOrder( -1 );
			mapping.setDetectHandlerMethodsInAncestorContexts( true );
			mapping.setContentNegotiationManager( contentNegotiationManager );
			return mapping;
		}
	}
}
