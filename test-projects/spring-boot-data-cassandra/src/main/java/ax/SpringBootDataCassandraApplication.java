package ax;

import com.datastax.driver.core.Session;
import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.CQLDataSet;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author Marc Vanbrabant
 * @since 1.0.0
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
public class SpringBootDataCassandraApplication
{
	public static void main( String[] args ) {
		SpringApplication.run( SpringBootDataCassandraApplication.class );
	}

	@Bean
	public CQLDataLoader dataLoader( Session session ) {
		CQLDataLoader cqlDataLoader = new CQLDataLoader( session );
		CQLDataSet cqlDataSet = new ClassPathCQLDataSet( "setup.cql" );
		cqlDataLoader.load( cqlDataSet );
		return cqlDataLoader;
	}
}
