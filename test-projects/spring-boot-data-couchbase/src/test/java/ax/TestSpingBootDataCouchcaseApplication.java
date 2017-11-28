package ax;

import ax.application.repositories.UserRepository;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.cluster.ClusterInfo;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpingBootDataCouchcaseApplication.class)
public class TestSpingBootDataCouchcaseApplication
{
	@Autowired
	private UserRepository repository;
	@Autowired
	private Bucket bucket;
	@Autowired
	private DefaultCouchbaseEnvironment couchbaseEnvironment;
	@Autowired
	private Cluster cluster;
	@Autowired
	private ClusterInfo clusterInfo;

	@Test
	public void shouldBootstrap() throws IOException {
		assertNotNull( repository );
		assertNotNull( bucket );
		assertNotNull( couchbaseEnvironment );
		assertNotNull( cluster );
		assertNotNull( clusterInfo );

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
