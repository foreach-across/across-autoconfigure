package ax.client;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@AcrossApplication(modules = AcrossWebModule.NAME)
public class SpringCloudConfigClientApplication
{
	@Component
	@RefreshScope
	class RootComponent
	{
		@Value("${fixed.message}")
		@Getter
		private String fixedMessage;

		@Value("${random.property}")
		@Getter
		private String randomValue;
	}

/*	@RestController
	@RefreshScope
	class ExampleController
	{

		@Value("${foo.bar}")
		private String value;

		@RequestMapping
		public String sayValue() {
			return value;
		}
	}
	*/

	public static void main( String[] args ) {
		new SpringApplicationBuilder()
				.sources( SpringCloudConfigClientApplication.class )
				.profiles( "client", "development" )
				.properties( "spring.cloud.config.enabled=true" )
				.run( args );
	}
}
