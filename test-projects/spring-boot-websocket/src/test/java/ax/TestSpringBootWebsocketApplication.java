package ax;

import ax.application.messages.GreetingMessage;
import ax.modules.websocket.messages.FarewellMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Steven Gentens
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSpringBootWebsocketApplication
{
	private static final String GREETING_ENDPOINT = "/app/hello";
	private static final String SUBSCRIBE_GREETING_ENDPOINT = "/topic/greetings";
	private static final String FAREWELL_ENDPOINT = "/app/farewell";
	private static final String SUBSCRIBE_FAREWELL_ENDPOINT = "/topic/farewell";
	private static final String GOODBYE_ENDPOINT = "/app/goodbye";
	private static final String SUBSCRIBE_GOODBYE_ENDPOINT = "/topic/goodbye";

	@org.springframework.beans.factory.annotation.Value("${local.server.port}")
	private int port;
	private String GREETING_URL;
	private String FAREWELL_URL;
	private String HANDLER_GOODBYE_URL;
	private String HANDLER_HELLO_URL;
	private CompletableFuture<GreetingMessage> greetingCompletableFuture;
	private CompletableFuture<FarewellMessage> farewellCompletableFuture;

	@BeforeEach
	public void setup() {
		greetingCompletableFuture = new CompletableFuture<>();
		farewellCompletableFuture = new CompletableFuture<>();
		String baseUrl = "ws://localhost:" + port;
		GREETING_URL = baseUrl + "/gs-guide-websocket";
		FAREWELL_URL = baseUrl + "/bar";
		HANDLER_GOODBYE_URL = baseUrl + "/app/goodbye";
		HANDLER_HELLO_URL = baseUrl + "/module/hello";
	}

	@Test
	public void applicationEndpoint() throws InterruptedException, ExecutionException, TimeoutException {
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
	public void moduleEndpoint() throws InterruptedException, ExecutionException, TimeoutException {
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
	public void simpMessagingTemplate() throws InterruptedException, ExecutionException, TimeoutException {
		WebSocketStompClient stompClient = getStompClient();

		StompSession stompSession = getStompSession( stompClient, FAREWELL_URL );

		stompSession.subscribe( SUBSCRIBE_GOODBYE_ENDPOINT, new CreateFarewellStompFrameHandler() );
		stompSession.send( GOODBYE_ENDPOINT, "Jocelyn" );

		FarewellMessage farewellMessage = farewellCompletableFuture.get( 10, SECONDS );

		assertNotNull( farewellMessage );
		assertTrue( farewellMessage.getContent().startsWith( "Goodbye, Jocelyn!" ) );

		disconnect( stompClient, stompSession );
	}

	@Test
	public void websocketScopedBeansAreEqual() throws InterruptedException, ExecutionException, TimeoutException {
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
	public void websocketScopedBeansAreDifferent() throws InterruptedException, ExecutionException, TimeoutException {
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

	@Test
	public void applicationWebSocketHandler() throws InterruptedException, ExecutionException, TimeoutException, IOException {
		SockJsClient socketClient = new SockJsClient( createTransportClient() );
		GoodbyeSocketHandler socketHandler = new GoodbyeSocketHandler();
		ListenableFuture<WebSocketSession> future = socketClient.doHandshake( socketHandler, HANDLER_GOODBYE_URL, "" );
		WebSocketSession session = future.get( 10, SECONDS );
		session.sendMessage( new TextMessage( "{\"name\":\"Jane\"}" ) );
		Thread.sleep( 4000 );
		assertEquals( "Bye Jane !", socketHandler.getMostRecentPayload() );
		session.close();
		socketClient.stop();
	}

	@Test
	public void moduleWebSocketHandler() throws InterruptedException, ExecutionException, TimeoutException, IOException {
		SockJsClient socketClient = new SockJsClient( createTransportClient() );
		GoodbyeSocketHandler socketHandler = new GoodbyeSocketHandler();
		ListenableFuture<WebSocketSession> future = socketClient.doHandshake( socketHandler, HANDLER_HELLO_URL, "" );
		WebSocketSession session = future.get( 10, SECONDS );
		session.sendMessage( new TextMessage( "{\"name\":\"Karen\"}" ) );
		Thread.sleep( 4000 );
		assertEquals( "Hello Karen !", socketHandler.getMostRecentPayload() );
		session.close();
		socketClient.stop();
	}

	private WebSocketStompClient getStompClient() {
		WebSocketStompClient stompClient = new WebSocketStompClient( new SockJsClient( createTransportClient() ) );
		stompClient.setMessageConverter( new MappingJackson2MessageConverter() );
		return stompClient;
	}

	private List<Transport> createTransportClient() {
		List<Transport> transports = new ArrayList<>( 1 );
		transports.add( new WebSocketTransport( new StandardWebSocketClient() ) );
		return transports;
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

	private class GoodbyeSocketHandler extends TextWebSocketHandler
	{
		private String mostRecentPayload;

		@Override
		public void handleMessage( WebSocketSession session, WebSocketMessage<?> message ) throws Exception {
			mostRecentPayload = (String) message.getPayload();
			System.out.println( mostRecentPayload );
		}

		public String getMostRecentPayload() {
			return mostRecentPayload;
		}
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
}
