package ax;

import lombok.SneakyThrows;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.SolrContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
abstract class AbstractIntegrationTest {

	public static String collectionName = "techproducts";
	// Keep aligned with solr.version in spring-boot-dependencies
	static SolrContainer container = new SolrContainer().withCollection(collectionName);

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

			// Do whatever you want with the client ...
			SolrClient client = new Http2SolrClient.Builder("http://" + container.getHost() + ":" + container.getSolrPort() + "/solr").build();
			client.ping(collectionName);

			TestPropertyValues.of(
					"spring.data.solr.host=http://" + container.getHost() + ":" + container.getSolrPort() + "/solr"
			).applyTo(context);
		}
	}
}