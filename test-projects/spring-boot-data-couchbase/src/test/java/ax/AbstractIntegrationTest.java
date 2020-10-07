package ax;

import lombok.SneakyThrows;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
abstract class AbstractIntegrationTest {

	static CouchbaseContainer container = new CouchbaseContainer().withBucket(new BucketDefinition("mybucket"));

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
			TestPropertyValues.of(
					"spring.couchbase.connection-string=" + container.getConnectionString(),
					"spring.couchbase.username=" + container.getUsername(),
					"spring.couchbase.password=" + container.getPassword()
			).applyTo(context);
		}
	}
}