package ax;

import ax.application.messages.GreetingMessage;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * @author Steven Gentens
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ITSpringBootWebsocketApplication
{

	@org.springframework.beans.factory.annotation.Value("${local.server.port}")
	private int port;

	private static final String GREETING_ENDPOINT = "/app/hello";
	private static final String SUBSCRIBE_GREETING_ENDPOINT = "/topic/greetings";

	private String URL;
	private CompletableFuture<GreetingMessage> completableFuture;

	@Before
	public void setup() {
		completableFuture = new CompletableFuture<>();
		URL = "ws://localhost:" + port + "/gs-guide-websocket";
	}

	@Test
	public void greetingEndpoint() throws InterruptedException, ExecutionException, TimeoutException {
		WebSocketStompClient stompClient = new WebSocketStompClient( new SockJsClient( createTransportClient() ) );
		stompClient.setMessageConverter( new MappingJackson2MessageConverter() );

		StompSession stompSession = stompClient.connect( URL, new StompSessionHandlerAdapter()
		{
		} ).get( 10, SECONDS );

		stompSession.subscribe( SUBSCRIBE_GREETING_ENDPOINT, new CreateGreetingStompFrameHandler() );
		stompSession.send( GREETING_ENDPOINT, "John" );

		GreetingMessage greetingMessage = completableFuture.get( 10, SECONDS );

		assertNotNull( greetingMessage );
		assertEquals( "Hello, John!", greetingMessage.getContent() );

	}

	private List<Transport> createTransportClient() {
		List<Transport> transports = new ArrayList<>( 1 );
		transports.add( new WebSocketTransport( new StandardWebSocketClient() ) );
		return transports;
	}

	private class CreateGreetingStompFrameHandler implements StompFrameHandler
	{
		@Override
		public Type getPayloadType( StompHeaders stompHeaders ) {
			return GreetingMessage.class;
		}

		@Override
		public void handleFrame( StompHeaders stompHeaders, Object o ) {
			completableFuture.complete( (GreetingMessage) o );
		}
	}
}
