package ax.modules.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author Steven Gentens
 */
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{
	@Override
	public void registerStompEndpoints( StompEndpointRegistry registry ) {
		registry.addEndpoint( "/bar" ).withSockJS();
	}

}
