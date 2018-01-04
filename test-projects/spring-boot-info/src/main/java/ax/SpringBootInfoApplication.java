package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author Marc Vanbrabant
 * @since 1.0.0
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
public class SpringBootInfoApplication
{
	public static void main( String[] args ) {
		SpringApplication.run( SpringBootInfoApplication.class );
	}

	@Component
	public class DeploymentInfoContributor implements InfoContributor
	{

		@Override
		public void contribute( Info.Builder builder ) {
			builder.withDetail( "deployment",
			                    Collections.singletonMap( "host", "s3-website-eu-west-1.amazonaws.com" ) );
		}

	}
}
