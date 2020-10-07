package ax;

import ax.application.business.Person;
import ax.application.repositories.PersonRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestSpringBootDataNeo4JApplication extends AbstractIntegrationTest
{
	@Autowired
	private PersonRepository repository;

	@Test
	public void shouldBootstrap() {
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
