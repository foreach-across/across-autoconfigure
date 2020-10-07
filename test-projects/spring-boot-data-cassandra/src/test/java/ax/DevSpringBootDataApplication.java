package ax;

import lombok.var;
import org.springframework.boot.SpringApplication;

public class DevSpringBootDataApplication {
	public static void main( String[] args ) {
		var application = SpringBootDataCassandraApplication.createSpringApplication();
		application.addInitializers(new AbstractIntegrationTest.Initializer());
		application.run(args);
	}
}
