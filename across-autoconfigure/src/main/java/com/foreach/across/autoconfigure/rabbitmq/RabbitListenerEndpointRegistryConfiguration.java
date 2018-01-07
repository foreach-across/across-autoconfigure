package com.foreach.across.autoconfigure.rabbitmq;

import org.springframework.amqp.rabbit.config.RabbitListenerConfigUtils;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Steven Gentens
 * @since 3.0.0
 */
@Configuration
public class RabbitListenerEndpointRegistryConfiguration
{
	@Bean(name = RabbitListenerConfigUtils.RABBIT_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME)
	public RabbitListenerEndpointRegistry defaultRabbitListenerEndpointRegistry() {
		return new RabbitListenerEndpointRegistry();
	}
}
