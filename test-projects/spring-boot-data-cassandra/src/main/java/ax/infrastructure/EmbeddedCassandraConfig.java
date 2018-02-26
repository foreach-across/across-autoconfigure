package ax.infrastructure;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author Steven Gentens
 * @since 3.0.0
 */
@Component
public class EmbeddedCassandraConfig
{
	@PostConstruct
	public void setup() throws IOException, InterruptedException, ConfigurationException, TTransportException {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra();
	}
}
