package ax;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
abstract class AbstractIntegrationTest {

	static int REDIS_PORT = 6379;
	static GenericContainer<?> container = new GenericContainer<>("redis:6.0.8").withExposedPorts(REDIS_PORT);

	@AfterClass
	public static void stop() {
		container.stop();
	}

	static class Initializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext context) {
			container.start();
			TestPropertyValues.of(
					"spring.redis.url=redis://" + container.getHost() + ":" + container.getMappedPort(REDIS_PORT)
			).applyTo(context);
		}
	}
}