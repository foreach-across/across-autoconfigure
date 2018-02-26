package ax;

import ax.application.business.Customer;
import ax.application.repositories.CustomerRepository;
import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import com.mongodb.MongoClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootDataMongoApplication.class)
public class TestSpringBootDataMongoApplication
{
	@Autowired
	private MongoClient mongoClient;

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private AcrossContextBeanRegistry beanRegistry;

	@Test
	public void repositoryIsCreatedInApplicationModule() {
		assertTrue( beanRegistry.moduleContainsLocalBean( "SpringBootDataMongoApplicationModule", "customerRepository" ) );
	}

	@Test
	public void shouldBootStrap() throws IOException {
		assertNotNull( mongoClient );

		repository.deleteAll();

		// save a couple of customers
		repository.save( new Customer( "Alice", "Smith" ) );
		repository.save( new Customer( "Bob", "Smith" ) );

		assertEquals( 2, repository.count() );
	}
}
