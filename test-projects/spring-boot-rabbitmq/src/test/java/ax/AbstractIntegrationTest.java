package ax;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.RabbitMQContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
abstract class AbstractIntegrationTest
{

	static RabbitMQContainer container = new RabbitMQContainer();

	@AfterClass
	public static void stop() {
		container.stop();
	}

	static class Initializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext>
	{

		@Override
		public void initialize( ConfigurableApplicationContext context ) {
			container.start();
			TestPropertyValues.of(
					"spring.rabbitmq.username=" + container.getAdminUsername(),
					"spring.rabbitmq.password=" + container.getAdminPassword(),
					"spring.rabbitmq.host=" + container.getHost(),
					"spring.rabbitmq.port=" + container.getMappedPort( 5672 )
			).applyTo( context );
		}
	}
}