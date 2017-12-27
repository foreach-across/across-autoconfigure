package com.foreach.across.autoconfigure.websocket;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpSessionScope;

/**
 * Creates a custom {@code websocket} scope.
 *
 * @author Steven Gentens
 * @see org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport
 * @since 3.0.0
 */
@Configuration
public class WebSocketScopeConfiguration
{
	@Bean
	public static CustomScopeConfigurer webSocketScopeConfigurer() {
		CustomScopeConfigurer configurer = new CustomScopeConfigurer();
		configurer.addScope( "websocket", new SimpSessionScope() );
		return configurer;
	}
}
