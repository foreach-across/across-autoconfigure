package com.foreach.across.autoconfigure.rabbitmq;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Add this annotation to an {@code @Configuration} or application class to configure
 * processing RabbitMQ messaging.
 *
 * @author Steven Gentens
 * @see org.springframework.amqp.rabbit.annotation.EnableRabbit
 * @since 3.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
@Import({ AcrossRabbitMQConfiguration.class })
public @interface EnableRabbit
{
}
