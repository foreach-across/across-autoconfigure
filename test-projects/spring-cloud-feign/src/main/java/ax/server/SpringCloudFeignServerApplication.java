package ax.server;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

/**
 * @author Gunther Van Geetsom
 * @since 1.0.1
 */
@AcrossApplication(modules = AcrossWebModule.NAME)
public class SpringCloudFeignServerApplication
{
	public static void main( String[] args ) {
		runApplication( args );
	}

	public static ServletWebServerApplicationContext runApplication( String... args ) {
		return (ServletWebServerApplicationContext) new SpringApplicationBuilder()
				.sources( SpringCloudFeignServerApplication.class )
				.profiles( "server" )
				.run( args );
	}
}
