package ax;

import ax.application.business.Person;
import ax.application.repositories.PersonRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestSpringBootDataRedis extends AbstractIntegrationTest
{
	@Autowired
	private PersonRepository repository;

	@Test
	public void shouldBootstrap() {
		assertNotNull( repository );

		repository.deleteAll();

		repository.save( new Person( "Alice", "Springs" ) );
		repository.save( new Person( "Charlotte", "Sits" ) );

		assertEquals( 2, repository.count() );

		// The following will return no results as the firstname field is not indexed in Redis
		assertEquals( 0, repository.findByFirstname( "Alice" ).size() );

		// The following will return results as the lastname field is indexed in Redis
		assertEquals( 1, repository.findByLastname( "Springs" ).size() );
	}
}
