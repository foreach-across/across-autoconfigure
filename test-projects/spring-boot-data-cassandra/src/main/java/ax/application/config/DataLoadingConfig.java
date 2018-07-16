package ax.application.config;

import com.datastax.driver.core.Session;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.CQLDataSet;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@Configuration
public class DataLoadingConfig
{
	@Bean
	public CQLDataLoader dataLoader( Session session ) {
		CQLDataLoader cqlDataLoader = new CQLDataLoader( session );
		CQLDataSet cqlDataSet = new ClassPathCQLDataSet( "setup.cql" );
		cqlDataLoader.load( cqlDataSet );
		return cqlDataLoader;
	}
}