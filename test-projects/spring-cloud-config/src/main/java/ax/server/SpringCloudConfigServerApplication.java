package ax.server;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@AcrossApplication(modules = AcrossWebModule.NAME)
@EnableConfigServer
public class SpringCloudConfigServerApplication
{
	public static void main( String[] args ) {
		runApplication( args );
	}

	public static ServletWebServerApplicationContext runApplication( String... args ) {
		return (ServletWebServerApplicationContext) new SpringApplicationBuilder()
				.sources( SpringCloudConfigServerApplication.class )
				.profiles( "server", "native" )
				.run( args );
	}
}
