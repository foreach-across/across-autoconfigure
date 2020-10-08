package ax;

import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootSessionApplication.class)
public class TestSpringBootSessionApplication
{
	@Value("${local.server.port}")
	private int port;

	@Autowired
	private AcrossContextBeanRegistry beanRegistry;

	@Test
	public void sessionExpiry() throws Exception {
		SessionRepository sessionRepository = beanRegistry.getBeanOfTypeFromModule(SpringBootSessionApplication.class.getSimpleName() + "Module", SessionRepository.class);
		assertTrue( sessionRepository instanceof JdbcIndexedSessionRepository);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> firstResponse = firstRequest( restTemplate, baseUri( "/" ) );
		String sessionId1 = firstResponse.getBody();
		Session sessionById = sessionRepository.findById(sessionId1);
		assertNotNull(sessionById);
		assertEquals( sessionId1, sessionById.getAttribute("user" ) );
		String cookie = firstResponse.getHeaders().getFirst( "Set-Cookie" );
		String sessionId2 = nextRequest( restTemplate, baseUri( "/" ), cookie ).getBody();
		assertEquals( sessionId1, sessionId2 );
		// Force an invalidation of a session server-side
		sessionRepository.deleteById(sessionId2);
		Thread.sleep( 1000 );
		String loginPage = nextRequest( restTemplate, baseUri( "/" ), cookie ).getBody();
		// New session should be created
		assertNotEquals( sessionId2, loginPage );
		Session newSession = sessionRepository.findById(loginPage);
		assertEquals( 1, newSession.getAttributeNames().size() );
		assertEquals( loginPage, newSession.getAttribute("user") );
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
