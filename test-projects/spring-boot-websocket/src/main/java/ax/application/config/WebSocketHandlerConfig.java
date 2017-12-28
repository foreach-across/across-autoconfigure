package ax.application.config;

import ax.application.handlers.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author Steven Gentens
 */
@Configuration
public class WebSocketHandlerConfig implements WebSocketConfigurer
{
	@Override
	public void registerWebSocketHandlers( WebSocketHandlerRegistry registry ) {
		registry.addHandler( new WebSocketHandler(), "/module", "/module/goodbye" ).withSockJS();
	}
}
