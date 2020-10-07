package ax;

import ax.application.business.Customer;
import ax.application.repositories.CustomerRepository;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.oss.driver.api.core.CqlSession;
import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class TestSpringBootDataCassandraApplication extends AbstractIntegrationTest
{
	@Autowired
	private CustomerRepository repository;

	@Autowired
	private AcrossContextBeanRegistry beanRegistry;

	@Test
	public void repositoryIsCreatedInApplicationModule() {
		assertTrue( beanRegistry.moduleContainsLocalBean( SpringBootDataCassandraApplication.class.getSimpleName() + "Module", "ax.application.repositories.CustomerRepository" ) );
	}

	@Test
	public void shouldBootstrap() {
		assertNotNull( repository );

		repository.deleteAll();

		assertEquals( 0, repository.count() );

		Customer alice = new Customer( UUIDs.timeBased(), "Alice", "Smith" );
		Customer bob = new Customer( UUIDs.timeBased(), "Bob", "Smith" );

		repository.save( alice );
		repository.save( bob );

		assertEquals( 2, repository.count() );

		assertEquals( alice, repository.findByFirstName( "Alice" ) );
		assertEquals( 2, repository.findByLastName( "Smith" ).size() );
	}
}
