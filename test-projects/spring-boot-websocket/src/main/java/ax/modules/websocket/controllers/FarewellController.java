package ax.modules.websocket.controllers;

import ax.application.messages.HelloMessage;
import ax.modules.websocket.messages.FarewellMessage;
import ax.modules.websocket.websocket.WebSocketScopeBean;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * @author Steven Gentens
 */
@Controller
@RequiredArgsConstructor
public class FarewellController
{
	private final WebSocketScopeBean webSocketScopeBean;

	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/farewell")
	@SendTo("/topic/farewell")
	public FarewellMessage farewell( HelloMessage message ) throws Exception {
		return new FarewellMessage( "Farewell, " + message.getName() + "! " + webSocketScopeBean.getUuid() );
	}

	@MessageMapping("/goodbye")
	public void goodbye( HelloMessage message ) throws Exception {
		simpMessagingTemplate.convertAndSend( "/topic/goodbye", new FarewellMessage( "Goodbye, " + message.getName() + "!" ) );
	}

	@Autowired
	@Lazy
	public void setSimpMessagingTemplate( SimpMessagingTemplate simpMessagingTemplate ) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}
}

