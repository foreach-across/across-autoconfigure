package ax;

import ax.application.business.User;
import ax.application.repositories.UserRepository;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSpringBootDataCouchbaseApplication extends AbstractIntegrationTest {
	@Autowired
	private UserRepository repository;
	@Autowired
	private AcrossContextBeanRegistry beanRegistry;
	@Autowired
	private Cluster cluster;
	@Autowired
	private ClusterEnvironment clusterEnvironment;

	@Test
	public void repositoryIsCreatedInApplicationModule() {
		assertTrue(beanRegistry.moduleContainsLocalBean(SpringBootDataCouchbaseApplication.class.getSimpleName() + "Module", "ax.application.repositories.UserRepository"));
	}

	@Test
	public void shouldBootstrap() {
		assertNotNull(repository);
		assertNotNull( cluster );
		assertNotNull( clusterEnvironment );

		repository.deleteAll();

		User user = saveUser();
		assertEquals(1, repository.count());
		Optional<User> byId = repository.findById(user.getId());
		assertTrue(byId.isPresent());
		assertEquals(user, byId.get());
	}

	private User saveUser() {
		User user = new User();
		user.setId(UUID.randomUUID().toString());
		user.setFirstName("Alice");
		user.setLastName("Smith");
		return repository.save(user);
	}
}
