package ax;

import lombok.var;

public class DevSpringBootDataApplication
{
	public static void main( String[] args ) {
		var application = SpringBootRabbitMQApplication.createSpringApplication();
		application.addInitializers( new AbstractIntegrationTest.Initializer() );
		application.run( args );
	}
}
