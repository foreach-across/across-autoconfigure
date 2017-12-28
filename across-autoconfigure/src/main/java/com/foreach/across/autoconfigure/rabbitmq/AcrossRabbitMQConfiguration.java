package com.foreach.across.autoconfigure.rabbitmq;

import com.foreach.across.core.context.bootstrap.AcrossBootstrapConfigurer;
import com.foreach.across.core.context.bootstrap.ModuleBootstrapConfig;
import org.springframework.amqp.rabbit.annotation.RabbitBootstrapConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * Ensures that the {@link RabbitBootstrapConfiguration} is created at the latest possible moment,
 * when all {@link org.springframework.amqp.rabbit.annotation.RabbitListener}s are created.
 * <p>
 * Exposes all {@link RabbitListenerConfigurer}s from the bootstrapped modules
 * so they can be picked up by the {@link RabbitBootstrapConfiguration}.
 *
 * @author Steven Gentens
 * @since 3.0.0
 */
@Configuration
public class AcrossRabbitMQConfiguration implements AcrossBootstrapConfigurer
{
	@Override
	public void configureModule( ModuleBootstrapConfig moduleConfiguration ) {
		moduleConfiguration.expose( RabbitListenerConfigurer.class );
		moduleConfiguration.addApplicationContextConfigurer( RabbitBootstrapConfiguration.class );
	}
}
