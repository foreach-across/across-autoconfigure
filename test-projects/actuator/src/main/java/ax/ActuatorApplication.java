package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author Arne Vandamme
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
public class ActuatorApplication
{
	@Component
	public class RootContextInfoContributor implements InfoContributor
	{
		@Override
		public void contribute( Info.Builder builder ) {
			builder.withDetail( "deployment",
			                    Collections.singletonMap( "host", "s3-website-eu-west-1.amazonaws.com" ) );
		}

	}

	public static void main( String[] args ) {
		SpringApplication springApplication = new SpringApplication( ActuatorApplication.class );
		springApplication.run( args );
	}
}
