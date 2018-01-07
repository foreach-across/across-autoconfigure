package com.foreach.across.autoconfigure.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.config.RabbitListenerConfigUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author Steven Gentens
 * @since 3.0.0
 */
@Configuration
public class RabbitListenerAnnotationBeanPostProcessorConfiguration
{
	@Bean(name = RabbitListenerConfigUtils.RABBIT_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public RabbitListenerAnnotationBeanPostProcessor rabbitListenerAnnotationProcessor() {
		return new RabbitListenerAnnotationBeanPostProcessor();
	}
}
