package ax;

import lombok.var;

public class DevSpringBootDataApplication {
	public static void main( String[] args ) {
		var application = SpringBootDataMongoApplication.createSpringApplication();
		application.addInitializers(new AbstractIntegrationTest.Initializer());
		application.run(args);
	}
}
