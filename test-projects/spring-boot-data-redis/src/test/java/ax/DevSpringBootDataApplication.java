package ax;

import lombok.var;

public class DevSpringBootDataApplication {
	public static void main( String[] args ) {
		var application = SpringBootDataRedis.createSpringApplication();
		application.addInitializers(new AbstractIntegrationTest.Initializer());
		application.run(args);
	}
}
