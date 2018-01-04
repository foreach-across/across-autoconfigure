package com.foreach.across.autoconfigure.websocket;

import com.foreach.across.core.context.bootstrap.AcrossBootstrapConfig;
import com.foreach.across.core.context.bootstrap.AcrossBootstrapConfigurer;
import com.foreach.across.core.context.bootstrap.ModuleBootstrapConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.DelegatingWebSocketConfiguration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

/**
 * Ensures that the {@link DelegatingWebSocketConfiguration} is created at the latest possible moment,
 * when all {@link WebSocketConfigurer}s are created.
 * <p>
 * Exposes all {@link WebSocketConfigurer}s from the bootstrapped modules
 * so they can be picked up by the {@link DelegatingWebSocketConfiguration}.
 *
 * @author Steven Gentens
 * @since 3.0.0
 */
@Configuration
public class AcrossWebSocketConfiguration implements AcrossBootstrapConfigurer
{
	@Override
	public void configureModule( ModuleBootstrapConfig moduleConfiguration ) {
		moduleConfiguration.expose( WebSocketConfigurer.class );
	}

	@Override
	public void configureContext( AcrossBootstrapConfig contextConfiguration ) {
		contextConfiguration.extendModule( CONTEXT_POSTPROCESSOR_MODULE, DelegatingWebSocketConfiguration.class );
	}
}
