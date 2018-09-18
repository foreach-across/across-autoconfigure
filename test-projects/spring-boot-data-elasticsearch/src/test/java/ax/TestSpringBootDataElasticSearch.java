package ax;

import ax.application.business.Person;
import ax.application.repositories.PersonRepository;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootDataElasticSearch.class)
public class TestSpringBootDataElasticSearch
{
	@Autowired
	private Client client;

	@Autowired
	private PersonRepository repository;

	@Test
	public void shouldBootstrap() throws IOException {
		assertNotNull( client );
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
