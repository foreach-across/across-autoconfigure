package ax;

import com.couchbase.mock.Bucket;
import com.couchbase.mock.BucketConfiguration;
import com.couchbase.mock.CouchbaseMock;
import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
public class SpingBootDataCouchcaseApplication
{
	public static void main( String[] args ) {
		SpringApplication.run( SpingBootDataCouchcaseApplication.class );
	}

	@PostConstruct
	public void setup() throws Exception {

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
