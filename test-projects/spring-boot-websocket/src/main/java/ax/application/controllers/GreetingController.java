package ax.application.controllers;

import ax.application.messages.GreetingMessage;
import ax.application.messages.HelloMessage;
import ax.modules.websocket.websocket.WebSocketScopeBean;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author Steven Gentens
 */
@Controller
@RequiredArgsConstructor
public class GreetingController
{
	private final WebSocketScopeBean webSocketScopeBean;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public GreetingMessage greeting( HelloMessage message ) throws Exception {
		Thread.sleep( 1000 ); // simulated delay
		return new GreetingMessage( "Hello, " + message.getName() + "! " + webSocketScopeBean.getUuid() );
	}
}
