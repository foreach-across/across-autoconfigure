package ax.infrastructure;

import com.couchbase.mock.Bucket;
import com.couchbase.mock.BucketConfiguration;
import com.couchbase.mock.CouchbaseMock;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Steven Gentens
 * @since 3.0.0
 */
@Component
public class EmbeddedCouchbaseConfig
{
	@PostConstruct
	public void setup() throws IOException, InterruptedException {
		BucketConfiguration bucketConfiguration = new BucketConfiguration();
		bucketConfiguration.numNodes = 1;
		bucketConfiguration.numReplicas = 0;
		bucketConfiguration.name = "default";
		bucketConfiguration.type = Bucket.BucketType.COUCHBASE;
		bucketConfiguration.password = "";

		CouchbaseMock couchbaseMock = new CouchbaseMock( 8091, Collections.singletonList( bucketConfiguration ) );
		couchbaseMock.start();
		couchbaseMock.waitForStartup();
	}
}
