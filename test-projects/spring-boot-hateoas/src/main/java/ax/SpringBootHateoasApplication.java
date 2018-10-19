package ax;

import ax.modules.custom.CustomModule;
import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Gunther Van Geetsom
 * @since 1.0.2
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
public class SpringBootHateoasApplication
{
	@Bean
	public CustomModule customModule() {
		return new CustomModule();
	}

	public static void main( String[] args ) {
		SpringApplication.run( SpringBootHateoasApplication.class );
	}
}
