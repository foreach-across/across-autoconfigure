package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;

/**
 * @author Arne Vandamme
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
public class ActuatorApplication
{
	public static void main( String[] args ) {
		SpringApplication springApplication = new SpringApplication( ActuatorApplication.class );
		springApplication.run( args );
	}
}
