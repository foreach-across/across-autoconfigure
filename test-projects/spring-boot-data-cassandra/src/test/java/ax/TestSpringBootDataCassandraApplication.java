package ax;

import ax.application.business.Customer;
import ax.application.repositories.CustomerRepository;
import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootDataCassandraApplication.class)
public class TestSpringBootDataCassandraApplication
{
	@Autowired
	private CustomerRepository repository;

	@Autowired
	private AcrossContextBeanRegistry beanRegistry;

	@Test
	public void repositoryIsCreatedInApplicationModule() {
		assertTrue( beanRegistry.moduleContainsLocalBean( "SpringBootDataCassandraApplicationModule", "customerRepository" ) );
	}

	@Test
	public void shouldBootstrap() throws IOException {
		assertNotNull( repository );

		repository.deleteAll();

		assertEquals( 0, repository.count() );

		Customer alice = new Customer( UUID.randomUUID(), "Alice", "Smith" );
		Customer bob = new Customer( UUID.randomUUID(), "Bob", "Smith" );

		repository.save( alice );
		repository.save( bob );

		assertEquals( 2, repository.count() );

		assertEquals( alice, repository.findByFirstName( "Alice" ) );
		assertEquals( 2, repository.findByLastName( "Smith" ).size() );
	}
}
