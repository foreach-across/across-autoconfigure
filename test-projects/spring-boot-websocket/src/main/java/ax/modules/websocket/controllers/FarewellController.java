package ax.modules.websocket.controllers;

import ax.application.messages.HelloMessage;
import ax.modules.websocket.messages.FarewellMessage;
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
public class FarewellController
{
	private final WebSocketScopeBean webSocketScopeBean;

	@MessageMapping("/farewell")
	@SendTo("/topic/farewell")
	public FarewellMessage farewell( HelloMessage message ) throws Exception {
		return new FarewellMessage( "Farewell, " + message.getName() + "! " + webSocketScopeBean.getUuid() );
	}
}

