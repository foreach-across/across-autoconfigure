package ax;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

// Todo: test does not seem to verify actual Spring session support
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootSessionApplication.class)
public class TestSpringBootSessionApplication
{
	@Value("${local.server.port}")
	private int port;

	@Test
	public void sessionExpiry() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> firstResponse = firstRequest( restTemplate, baseUri( "/" ) );
		String sessionId1 = firstResponse.getBody();
		String cookie = firstResponse.getHeaders().getFirst( "Set-Cookie" );
		String sessionId2 = nextRequest( restTemplate, baseUri( "/" ), cookie ).getBody();
		assertEquals( sessionId1, sessionId2 );
		Thread.sleep( 1000 );
		String loginPage = nextRequest( restTemplate, baseUri( "/" ), cookie ).getBody();
		//assertEquals(loginPage).containsIgnoringCase("login");
	}

	private URI baseUri( String path ) throws URISyntaxException {
		return new URI( "http://localhost:" + port + path );
	}

	private ResponseEntity<String> firstRequest( RestTemplate restTemplate, URI uri ) {
		HttpHeaders headers = new HttpHeaders();
		headers.set( "Authorization", "Basic "
				+ Base64.getEncoder().encodeToString( "user:password".getBytes() ) );
		RequestEntity<Object> request = new RequestEntity<>( headers, HttpMethod.GET, uri );
		return restTemplate.exchange( request, String.class );
	}

	private ResponseEntity<String> nextRequest( RestTemplate restTemplate, URI uri,
	                                            String cookie ) {
		HttpHeaders headers = new HttpHeaders();
		headers.set( "Cookie", cookie );
		RequestEntity<Object> request = new RequestEntity<>( headers, HttpMethod.GET, uri );
		return restTemplate.exchange( request, String.class );
	}
}
