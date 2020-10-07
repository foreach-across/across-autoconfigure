package ax;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import lombok.SneakyThrows;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.CassandraContainer;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
abstract class AbstractIntegrationTest {

	static CassandraContainer<?> container = new CassandraContainer<>().withInitScript("setup.cql");

	@AfterClass
	public static void stop() {
		container.stop();
	}

	static class Initializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		@SneakyThrows
		public void initialize(ConfigurableApplicationContext context) {
			container.start();

			Cluster cluster = container.getCluster();

			try(Session session = cluster.connect()) {

				session.execute("CREATE KEYSPACE IF NOT EXISTS testKeySpace WITH replication = \n" +
						"{'class':'SimpleStrategy','replication_factor':'1'};");

				List<KeyspaceMetadata> keyspaces = session.getCluster().getMetadata().getKeyspaces();
				List<KeyspaceMetadata> filteredKeyspaces = keyspaces
						.stream()
						.filter(km -> km.getName().equals("testkeyspace"))
						.collect(Collectors.toList());

				assertEquals(1, filteredKeyspaces.size());
			}

			TestPropertyValues.of(
					"spring.data.cassandra.port=" + container.getMappedPort(CassandraContainer.CQL_PORT),
					"spring.data.cassandra.localDatacenter=datacenter1",
					"spring.data.cassandra.keyspaceName=testKeySpace",
					"spring.data.cassandra.contactPoints=" + container.getHost() + ":" + container.getMappedPort(CassandraContainer.CQL_PORT)
			).applyTo(context);
		}
	}
}