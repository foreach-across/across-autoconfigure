package ax;

import ax.client.SpringCloudFeignClientApplication;
import ax.client.application.domain.book.BookClient;
import ax.client.application.domain.book.BookClientResource;
import ax.server.SpringCloudFeignServerApplication;
import ax.server.application.domain.book.BookResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Gunther Van Geetsom
 * @since 1.0.1
 */
public class TestSpringCloudFeign
{
	private static ServletWebServerApplicationContext serverContext, clientContext;
	private static RestTemplate server;
	private static BookClient client;
	private static String serverUrl;
	private static String clientUrl;

	@BeforeClass
	public static void start() {
		serverContext = SpringCloudFeignServerApplication.runApplication( "--server.port=0" );
		int serverPort = serverContext.getWebServer().getPort();
		serverUrl = "http://localhost:" + serverPort;
		server = new RestTemplateBuilder().rootUri( serverUrl ).build();

		clientUrl = "http://localhost:" + serverPort;
		clientContext = SpringCloudFeignClientApplication.runApplication( "--server.port=0", "--book.api.url=" + clientUrl );
		client = clientContext.getBean( BookClient.class );
	}

	@AfterClass
	public static void stop() {
		clientContext.close();
		serverContext.close();
	}

	@Test
	public void feignClientShouldWork() {
		URI serverUri = UriComponentsBuilder.fromHttpUrl( serverUrl ).path( "books" ).build().toUri();

		List<BookResource> booksFromServer = Arrays.asList( server.getForEntity( serverUri, BookResource[].class ).getBody() );
		List<BookClientResource> booksFromClient = client.getBooks();

		assertEquals( booksFromServer.size(), booksFromClient.size() );
		for ( int i = 0; i < booksFromServer.size(); i++ ) {
			BookResource bookFromServer = booksFromServer.get( i );
			BookClientResource bookFromClient = booksFromClient.get( i );
			assertBookResource( bookFromServer, bookFromClient );
		}
	}

	@Test
	public void feignClientWithPathVariableAndParam() {
		URI serverUri = UriComponentsBuilder.fromHttpUrl( serverUrl )
		                                    .path( "book/1" )
		                                    .queryParam( "publishedAfter", "1/1/17" ).build()
		                                    .toUri();

		BookResource bookFromServer = server.getForEntity( serverUri, BookResource.class ).getBody();
		BookClientResource bookFromClient = client.getOneBook( 1L, LocalDate.of( 2017, 1, 1 ) );

		assertBookResource( bookFromServer, bookFromClient );
	}

	@Test
	public void feignShouldFallbackWithHystrix() {
		BookClientResource fallbackBook = client.getOneBook( 1L, null ); // publishedAfter is a required parameter

		assertNotNull( fallbackBook );
		assertEquals( -99, fallbackBook.getId() );
	}

	@Test
	public void actuatorWithFeignAndHystrixShouldGiveStatus() {
		URI healthUri = UriComponentsBuilder.fromHttpUrl( clientUrl )
		                                    .path( "/actuator/health" )
		                                    .build()
		                                    .toUri();

		assertNotNull( new RestTemplate().getForEntity( healthUri, HashMap.class ).getBody().get( "hystrix" ) );
	}

	private void assertBookResource( BookResource bookFromServer, BookClientResource bookFromClient ) {
		assertEquals( bookFromServer.getId(), bookFromClient.getId() );
		assertEquals( bookFromServer.getName(), bookFromClient.getName() );
		assertEquals( bookFromServer.getAuthor(), bookFromClient.getAuthor() );
		assertEquals( bookFromServer.getPrice(), bookFromClient.getPrice(), 0 );
		assertEquals( bookFromServer.getPublishedOn(), bookFromClient.getPublishedOn() );
	}
}
