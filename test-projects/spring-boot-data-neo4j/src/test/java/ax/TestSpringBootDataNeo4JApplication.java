package ax;

import ax.application.business.Person;
import ax.application.repositories.PersonRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootDataNeo4jApplication.class)
public class TestSpringBootDataNeo4JApplication
{
	@Autowired
	private PersonRepository repository;

	@Test
	public void shouldBootstrap() throws IOException {
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
