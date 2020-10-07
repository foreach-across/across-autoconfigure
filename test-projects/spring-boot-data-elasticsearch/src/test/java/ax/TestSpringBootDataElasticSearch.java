package ax;

import ax.application.business.Person;
import ax.application.repositories.PersonRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestSpringBootDataElasticSearch extends AbstractIntegrationTest
{
	@Autowired
	private PersonRepository repository;

	@Test
	public void shouldBootstrap() {
		assertNotNull( repository );

		repository.deleteAll();

		repository.save( new Person( "Alice", "Chan" ) );
		repository.save( new Person( "Bob", "Builder" ) );
		repository.save( new Person( "Chris", "Adolf" ) );

		assertEquals( 3, repository.count() );

		List<Person> persons = repository.findByFirstname( "Alice" );
		assertEquals( 1, persons.size() );
	}
}
