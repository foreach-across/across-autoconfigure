package com.foreach.across.autoconfigure.websocket;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Add this annotation to an {@code @Configuration} or application class to configure
 * processing WebSocket requests.
 *
 * @author Steven Gentens
 * @see org.springframework.web.socket.config.annotation.EnableWebSocket
 * @since 3.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
@Import({ AcrossWebSocketConfiguration.class })
public @interface EnableWebSocket
{
}
