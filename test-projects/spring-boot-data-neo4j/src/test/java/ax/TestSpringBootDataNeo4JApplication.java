package ax;

import ax.application.business.Person;
import ax.application.repositories.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootDataNeo4jApplication.class)
public class TestSpringBootDataNeo4JApplication
{

	@Autowired
	private Session session;
	@Autowired
	private PersonRepository repository;

	@Test
	public void shouldBootstrap() throws IOException {
		assertNotNull( session );
		assertNotNull( repository );

		repository.deleteAll();

		repository.save( new Person( "Alice" ) );
		repository.save( new Person( "Bob" ) );

		assertEquals( 2, repository.count() );

		Person alice = repository.findByName( "Alice" );
		assertNotNull( alice );
		assertEquals( "Alice", alice.getName() );
	}
}
