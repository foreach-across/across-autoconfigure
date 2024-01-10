package ax;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ActuatorApplication.class)
public class TestActuatorEndpoints
{
	@Value("${local.server.port}")
	private int port;

	@Autowired
	private GitProperties gitProperties;

	@Autowired
	private BuildProperties buildProperties;

	private TestRestTemplate restTemplate;

	@Autowired
	public void createRestTemplate( RestTemplateBuilder restTemplateBuilder ) {
		restTemplate = new TestRestTemplate( restTemplateBuilder.rootUri( "http://localhost:" + port + "/actuator" ) );
	}

	@Test
	@SuppressWarnings("all")
	public void healthEndpointShouldHaveCustomIndicator() {
		val response = restTemplate.getForEntity( "/health", Map.class );
		assertNotNull( response );
		assertEquals( HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode() );

		Map data = response.getBody();
		Map customIndicator = (Map) ( (Map) data.get( "components" ) ).get( "my" );
		assertNotNull( customIndicator );
		assertEquals( "DOWN", customIndicator.get( "status" ) );
	}

	@Test
	public void gitAndBuildPropertiesShouldBeAvailable() {
		assertNotNull( gitProperties );
		assertNotNull( buildProperties );
	}

	@Test
	public void infoShouldProvideGitAndBuildProperties() {
		ResponseEntity<Map> entity = restTemplate.getForEntity( "/info", Map.class );

		assertNotNull( entity );
		Map body = entity.getBody();

		assertNotNull( body );
		assertEquals( 4, body.size() );

		Map git = (Map) body.get( "git" );
		assertEquals( "6147f97", ( (Map) git.get( "commit" ) ).get( "id" ) );

		Map build = (Map) body.get( "build" );
		assertEquals( 5, build.size() );
		assertEquals( "spring-boot-info", build.get( "artifact" ) );
		assertEquals( "test-projects:spring-boot-info", build.get( "name" ) );

		Map deployment = (Map) body.get( "deployment" );
		assertEquals( "s3-website-eu-west-1.amazonaws.com", deployment.get( "host" ) );

		// EnvironmentInfoContributor is disabled by default in 2.6:
		// https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.6-Release-Notes#actuator-env-infocontributor-disabled-by-default
		// https://github.com/spring-projects/spring-boot/issues/28311
		// assertEquals( "actuator", body.get( "application-name" ) );
		assertEquals( "hello", body.get( "module.says" ) );
	}
}
