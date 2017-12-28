package ax.modules.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @author Steven Gentens
 */
@Configuration
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer
{
	@Override
	public void registerStompEndpoints( StompEndpointRegistry registry ) {
		registry.addEndpoint( "/bar" ).withSockJS();
	}

}
