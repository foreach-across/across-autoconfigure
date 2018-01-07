package com.foreach.across.autoconfigure.rabbitmq;

import com.foreach.across.core.context.bootstrap.AcrossBootstrapConfigurer;
import com.foreach.across.core.context.bootstrap.ModuleBootstrapConfig;
import org.springframework.amqp.rabbit.annotation.RabbitBootstrapConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * Ensures that a RabbitListenerEndpointRegistry is present and that {@link org.springframework.amqp.rabbit.annotation.RabbitListener} annotations are picked up.
 * <p>
 * Exposes all {@link RabbitListenerConfigurer}s from the bootstrapped modules
 * to configure the default {@link org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory}.
 *
 * @author Steven Gentens
 * @since 3.0.0
 * @see RabbitBootstrapConfiguration
 */
@Configuration
public class AcrossRabbitMQConfiguration implements AcrossBootstrapConfigurer
{
	@Override
	public void configureModule( ModuleBootstrapConfig moduleConfiguration ) {
		if ( moduleConfiguration.getBootstrapIndex() == 1 ) {
			moduleConfiguration.addApplicationContextConfigurer( RabbitListenerEndpointRegistryConfiguration.class );
		}
		moduleConfiguration.addApplicationContextConfigurer( RabbitListenerAnnotationBeanPostProcessorConfiguration.class );
		moduleConfiguration.expose( RabbitListenerConfigurer.class );
	}

}
