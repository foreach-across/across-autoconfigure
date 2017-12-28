package com.foreach.across.autoconfigure.websocket;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Add this annotation to an {@code @Configuration} or application class to enable broker-backed
 * messaging over WebSocket using a higher-level messaging sub-protocol.
 *
 * @author Steven Gentens
 * @see org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
 * @since 3.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
@Import({ AcrossWebSocketMessageBrokerConfiguration.class })
public @interface EnableWebSocketMessageBroker
{
}
