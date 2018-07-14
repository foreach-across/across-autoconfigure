package ax;

import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ActuatorApplication.class)
public class TestActuatorEndpoints
{
	@Value("${local.server.port}")
	private int port;

	private TestRestTemplate restTemplate;

	@Autowired
	public void createRestTemplate( RestTemplateBuilder restTemplateBuilder ) {
		restTemplate = new TestRestTemplate( restTemplateBuilder.rootUri( "http://localhost:" + port + "/actuator" ) );
	}

	@Test
	@SuppressWarnings( "all" )
	public void healthEndpointShouldHaveCustomIndicator() {
		val response = restTemplate.getForEntity( "/health", HashMap.class );
		assertNotNull( response );
		assertEquals( HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode() );

		Map data = response.getBody();
		Map customIndicator = (Map) ((Map) data.get( "details" )).get( "my" );
		assertNotNull( customIndicator );
		assertEquals( "DOWN", customIndicator.get( "status" ) );
	}
}
