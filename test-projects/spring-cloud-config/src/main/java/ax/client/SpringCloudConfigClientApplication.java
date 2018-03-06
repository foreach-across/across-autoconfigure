package ax.client;

import ax.client.application.BoundComponent;
import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@AcrossApplication(modules = AcrossWebModule.NAME)
public class SpringCloudConfigClientApplication
{
	@Bean
	@RefreshScope
	public BoundComponent rootComponent() {
		return new BoundComponent();
	}

	@RestController
	@RefreshScope
	class ApplicationCustomPropertyController
	{
		@Value("${custom.property:}")
		private String customProperty;

		@GetMapping("/application/customProperty")
		public String renderCustomPropertyValue() {
			return customProperty;
		}
	}

	public static void main( String[] args ) {
		runApplication( args );
	}

	public static EmbeddedWebApplicationContext runApplication( String... args ) {
		return (EmbeddedWebApplicationContext) new SpringApplicationBuilder()
				.sources( SpringCloudConfigClientApplication.class )
				.profiles( "client", "development" )
				.properties( "spring.cloud.config.enabled=true" )
				.run( args );
	}
}
