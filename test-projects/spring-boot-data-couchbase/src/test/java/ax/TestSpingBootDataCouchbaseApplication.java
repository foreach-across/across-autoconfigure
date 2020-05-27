package ax;

import ax.application.repositories.UserRepository;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootDataCouchbaseApplication.class)
public class TestSpingBootDataCouchbaseApplication
{
	@Autowired
	private UserRepository repository;
	@Autowired
	private Bucket bucket;
	@Autowired
	private ClusterEnvironment clusterEnvironment;
	@Autowired
	private Cluster cluster;
	@Autowired
	private AcrossContextBeanRegistry beanRegistry;

	@Test
	public void repositoryIsCreatedInApplicationModule() {
		assertTrue( beanRegistry.moduleContainsLocalBean( SpringBootDataCouchbaseApplication.class.getSimpleName() + "Module", "userRepository" ) );
	}

	@Test
	public void shouldBootstrap() throws IOException {
		assertNotNull( repository );
		assertNotNull( bucket );
		assertNotNull( clusterEnvironment );
		assertNotNull( cluster );

		repository.deleteAll();

		// TODO: CouchbaseMock doesn't seem to persist data correctly
		// TODO: It also doesn't support bootstrapCarrierEnabled=false (java.net.ConnectException: Connection refused: localhost/127.0.0.1:11210)
		//assertEquals( 1, repository.count() );
		//assertEquals( user, repository.findOne( user.getId()));
	}

/*    private User saveUser() {
        User user = new User();
        user.setId( UUID.randomUUID().toString());
        user.setFirstName("Alice");
        user.setLastName("Smith");
        return repository.save(user);
    }*/
}
