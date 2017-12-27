package ax;

import ax.application.messages.GreetingMessage;
import ax.modules.websocket.messages.FarewellMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static junit.framework.TestCase.*;

/**
 * @author Steven Gentens
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSpringBootWebsocketApplication
{

	@org.springframework.beans.factory.annotation.Value("${local.server.port}")
	private int port;

	private static final String GREETING_ENDPOINT = "/app/hello";
	private static final String SUBSCRIBE_GREETING_ENDPOINT = "/topic/greetings";
	private static final String FAREWELL_ENDPOINT = "/app/farewell";
	private static final String SUBSCRIBE_FAREWELL_ENDPOINT = "/topic/farewell";

	private String GREETING_URL;
	private String FAREWELL_URL;
	private CompletableFuture<GreetingMessage> greetingCompletableFuture;
	private CompletableFuture<FarewellMessage> farewellCompletableFuture;

	@Before
	public void setup() {
		greetingCompletableFuture = new CompletableFuture<>();
		farewellCompletableFuture = new CompletableFuture<>();
		GREETING_URL = "ws://localhost:" + port + "/gs-guide-websocket";
		FAREWELL_URL = "ws://localhost:" + port + "/bar";
	}

	@Test
	public void greetingEndpoint() throws InterruptedException, ExecutionException, TimeoutException {
		WebSocketStompClient stompClient = getStompClient();

		StompSession stompSession = getStompSession( stompClient, GREETING_URL );

		stompSession.subscribe( SUBSCRIBE_GREETING_ENDPOINT, new CreateGreetingStompFrameHandler() );
		stompSession.send( GREETING_ENDPOINT, "John" );

		GreetingMessage greetingMessage = greetingCompletableFuture.get( 10, SECONDS );

		assertNotNull( greetingMessage );
		assertTrue( greetingMessage.getContent().startsWith( "Hello, John!" ) );

		disconnect( stompClient, stompSession );
	}

	@Test
	public void farewellEndpoint() throws InterruptedException, ExecutionException, TimeoutException {
		WebSocketStompClient stompClient = getStompClient();

		StompSession stompSession = getStompSession( stompClient, FAREWELL_URL );

		stompSession.subscribe( SUBSCRIBE_FAREWELL_ENDPOINT, new CreateFarewellStompFrameHandler() );
		stompSession.send( FAREWELL_ENDPOINT, "Johnson" );

		FarewellMessage farewellMessage = farewellCompletableFuture.get( 10, SECONDS );

		assertNotNull( farewellMessage );
		assertTrue( farewellMessage.getContent().startsWith( "Farewell, Johnson!" ) );

		disconnect( stompClient, stompSession );
	}

	@Test
	public void testScopedBeansSame() throws InterruptedException, ExecutionException, TimeoutException {
		WebSocketStompClient stompClient = getStompClient();

		StompSession stompSession = getStompSession( stompClient, FAREWELL_URL );

		stompSession.subscribe( SUBSCRIBE_FAREWELL_ENDPOINT, new CreateFarewellStompFrameHandler() );
		stompSession.send( FAREWELL_ENDPOINT, "Johnson" );

		FarewellMessage farewellMessage = farewellCompletableFuture.get( 10, SECONDS );

		assertNotNull( farewellMessage );
		String farewellContent = "Farewell, Johnson!";
		assertTrue( farewellMessage.getContent().startsWith( farewellContent ) );

		stompSession.subscribe( SUBSCRIBE_GREETING_ENDPOINT, new CreateGreetingStompFrameHandler() );
		stompSession.send( GREETING_ENDPOINT, "John" );

		GreetingMessage greetingMessage = greetingCompletableFuture.get( 10, SECONDS );

		assertNotNull( greetingMessage );
		String greetingContent = "Hello, John!";
		assertTrue( greetingMessage.getContent().startsWith( greetingContent ) );

		assertTrue( farewellMessage.getContent().replace( farewellContent, "" ).equals( greetingMessage.getContent().replace( greetingContent, "" ) ) );

		disconnect( stompClient, stompSession );
	}

	@Test
	public void testScopedBeansDifferent() throws InterruptedException, ExecutionException, TimeoutException {
		WebSocketStompClient stompClient = getStompClient();
		StompSession stompSession = getStompSession( stompClient, FAREWELL_URL );

		stompSession.subscribe( SUBSCRIBE_FAREWELL_ENDPOINT, new CreateFarewellStompFrameHandler() );
		stompSession.send( FAREWELL_ENDPOINT, "Johnson" );

		FarewellMessage farewellMessage = farewellCompletableFuture.get( 10, SECONDS );

		assertNotNull( farewellMessage );
		String farewellContent = "Farewell, Johnson!";
		assertTrue( farewellMessage.getContent().startsWith( farewellContent ) );

		disconnect( stompClient, stompSession );
		stompClient = getStompClient();
		stompSession = getStompSession( stompClient, FAREWELL_URL );

		stompSession.subscribe( SUBSCRIBE_GREETING_ENDPOINT, new CreateGreetingStompFrameHandler() );
		stompSession.send( GREETING_ENDPOINT, "John" );

		GreetingMessage greetingMessage = greetingCompletableFuture.get( 10, SECONDS );

		assertNotNull( greetingMessage );
		String greetingContent = "Hello, John!";
		assertTrue( greetingMessage.getContent().startsWith( greetingContent ) );

		assertFalse( farewellMessage.getContent().replace( farewellContent, "" ).equals( greetingMessage.getContent().replace( greetingContent, "" ) ) );

		disconnect( stompClient, stompSession );
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
			greetingCompletableFuture.complete( (GreetingMessage) o );
		}
	}

	private class CreateFarewellStompFrameHandler implements StompFrameHandler
	{
		@Override
		public Type getPayloadType( StompHeaders stompHeaders ) {
			return FarewellMessage.class;
		}

		@Override
		public void handleFrame( StompHeaders stompHeaders, Object o ) {
			farewellCompletableFuture.complete( (FarewellMessage) o );
		}
	}

	private WebSocketStompClient getStompClient() {
		WebSocketStompClient stompClient = new WebSocketStompClient( new SockJsClient( createTransportClient() ) );
		stompClient.setMessageConverter( new MappingJackson2MessageConverter() );
		return stompClient;
	}

	private StompSession getStompSession( WebSocketStompClient stompClient, String url ) throws InterruptedException, ExecutionException, TimeoutException {
		return stompClient.connect( url, new StompSessionHandlerAdapter()
		{
		} ).get( 10, SECONDS );
	}

	private void disconnect( WebSocketStompClient stompClient, StompSession stompSession ) {
		stompSession.disconnect();
		stompClient.stop();
	}
}
