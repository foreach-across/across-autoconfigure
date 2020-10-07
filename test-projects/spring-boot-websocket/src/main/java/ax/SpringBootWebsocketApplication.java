package ax;

import ax.modules.websocket.CustomWebsocketModule;
import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

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

	@Bean
	public CustomWebsocketModule customWebsocketModule() {
		return new CustomWebsocketModule();
	}
}
