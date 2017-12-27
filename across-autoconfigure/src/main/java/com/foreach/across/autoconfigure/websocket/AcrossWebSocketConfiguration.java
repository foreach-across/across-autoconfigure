package com.foreach.across.autoconfigure.websocket;

import com.foreach.across.core.context.bootstrap.AcrossBootstrapConfig;
import com.foreach.across.core.context.bootstrap.AcrossBootstrapConfigurer;
import com.foreach.across.core.context.bootstrap.ModuleBootstrapConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Ensures that the DelegatingWebSocketMessageBrokerConfiguration is properly created,
 * as well as that the {@code websocket} scope is configured for every bootstrapped module.
 *
 * @author Steven Gentens
 * @since 3.0.0
 */
@Configuration
public class AcrossWebSocketConfiguration implements AcrossBootstrapConfigurer
{
	@Override
	public void configureModule( ModuleBootstrapConfig moduleConfiguration ) {
		moduleConfiguration.addApplicationContextConfigurer( WebSocketScopeConfiguration.class );
		moduleConfiguration.expose( WebSocketMessageBrokerConfigurer.class );
	}

	@Override
	public void configureContext( AcrossBootstrapConfig contextConfiguration ) {
		contextConfiguration.extendModule( CONTEXT_POSTPROCESSOR_MODULE, DelegatingWebSocketMessageBrokerConfiguration.class );
	}
}