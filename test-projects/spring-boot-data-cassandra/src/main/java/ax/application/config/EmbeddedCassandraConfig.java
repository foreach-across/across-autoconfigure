package ax.application.config;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author Steven Gentens
 * @since
 */
@Configuration
@AutoConfigureBefore(CassandraDataAutoConfiguration.class)
public class EmbeddedCassandraConfig
{
	@PostConstruct
	public void setup() throws IOException, InterruptedException, ConfigurationException, TTransportException {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra();
	}
}
