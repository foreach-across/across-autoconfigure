package ax;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DirtiesContext
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootInfoApplication.class)
public class TestSpringBootInfoApplication
{

	@Autowired
	private GitProperties gitProperties;
	@Autowired
	private BuildProperties buildProperties;
	@Value("${local.server.port}")
	private int port;

	// Todo: merge into actuator test project
	@Test
	public void shouldBootStrapAndExposeBuildInfo() throws IOException, InterruptedException {
		assertNotNull( gitProperties );
		assertNotNull( buildProperties );

		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<Map> entity = restTemplate.getForEntity( "http://localhost:" + port + "/actuator/info", Map.class );

		assertNotNull( entity );
		Map body = entity.getBody();

		assertEquals( 3, body.size() );

		Map git = (Map) body.get( "git" );
		assertEquals( "6147f97", ( (Map) git.get( "commit" ) ).get( "id" ) );

		Map build = (Map) body.get( "build" );
		assertEquals( 5, build.size() );
		assertEquals( "spring-boot-info", build.get( "artifact" ) );
		assertEquals( "test-projects:spring-boot-info", build.get( "name" ) );

		Map deployment = (Map) body.get( "deployment" );
		assertEquals( "s3-website-eu-west-1.amazonaws.com", deployment.get( "host" ) );
	}
}
