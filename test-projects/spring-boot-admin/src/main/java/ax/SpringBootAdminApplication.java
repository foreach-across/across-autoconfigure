package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Marc Vanbrabant
 */
@AcrossApplication(
		modules = {
				AcrossWebModule.NAME
		}
)
@EnableAdminServer
public class SpringBootAdminApplication
{
	public static void main( String[] args ) {
		SpringApplication springApplication = new SpringApplication( SpringBootAdminApplication.class );
		springApplication.run( args );
	}

	@Bean
	public WebMvcConfigurer asyncThreadPoolTaskExecutor() {
		return new WebMvcConfigurer() {
			@Override
			public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
				// Required for the WebClient which returns reactive streams
				configurer.setTaskExecutor(new ThreadPoolTaskExecutor());
			}
		};
	}
}
