package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author Marc Vanbrabant
 * @since 1.0.0
 */
@AcrossApplication(modules = AcrossWebModule.NAME)
public class SpringBootDataMongoApplication
{
	public static void main( String[] args ) {
		new SpringApplicationBuilder( SpringBootDataMongoApplication.class ).build().run();
	}
}
