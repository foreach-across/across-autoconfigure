package ax.client;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;

/**
 * @author Gunther Van Geetsom
 * @since 1.0.1
 */
@AcrossApplication(modules = AcrossWebModule.NAME)
public class SpringCloudFeignClientApplication
{
	public static void main( String[] args ) {
		runApplication( args );
	}

	public static EmbeddedWebApplicationContext runApplication( String... args ) {
		return (EmbeddedWebApplicationContext) new SpringApplicationBuilder()
				.sources( SpringCloudFeignClientApplication.class )
				.profiles( "client" )
				.run( args );
	}
}
