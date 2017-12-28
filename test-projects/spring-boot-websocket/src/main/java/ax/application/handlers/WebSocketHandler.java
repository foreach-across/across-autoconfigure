package ax.application.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Steven Gentens
 */
@Component
public class WebSocketHandler extends TextWebSocketHandler
{
	private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handleTextMessage( WebSocketSession session, TextMessage message )
			throws InterruptedException, IOException {

		for ( WebSocketSession webSocketSession : sessions ) {
			Map value = objectMapper.readValue( message.getPayload(), new TypeReference<Map<String, String>>()
			{
			} );
			webSocketSession.sendMessage( new TextMessage( "Hello " + value.get( "name" ) + " !" ) );
		}
	}

	@Override
	public void afterConnectionEstablished( WebSocketSession session ) throws Exception {
		//the messages will be broadcasted to all users.
		sessions.add( session );
	}
}
