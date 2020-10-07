package ax;

import ax.application.business.Customer;
import ax.application.repositories.CustomerRepository;
import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import com.mongodb.client.MongoClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class TestSpringBootDataMongoApplication extends AbstractIntegrationTest
{
	@Autowired
	private MongoClient mongoClient;

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private AcrossContextBeanRegistry beanRegistry;

	@Test
	public void repositoryIsCreatedInApplicationModule() {
		assertTrue( beanRegistry.moduleContainsLocalBean( "SpringBootDataMongoApplicationModule", "ax.application.repositories.CustomerRepository" ) );
	}

	@Test
	public void shouldBootStrap() {
		assertNotNull( mongoClient );

		repository.deleteAll();

		// save a couple of customers
		repository.save( new Customer( "Alice", "Smith" ) );
		repository.save( new Customer( "Bob", "Smith" ) );

		assertEquals( 2, repository.count() );
	}
}
