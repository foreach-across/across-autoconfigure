package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;

/**
 * @author Steven Gentens
 * @since 1.0.0
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
public class SpringBootWebsocketApplication
{
	public static void main( String[] args ) {
		SpringApplication.run( SpringBootWebsocketApplication.class );
	}
}