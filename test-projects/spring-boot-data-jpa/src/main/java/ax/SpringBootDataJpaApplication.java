package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * @author Arne Vandamme
 */
@AcrossApplication
public class SpringBootDataJpaApplication
{
	@Profile("across-web")
	@Bean
	public AcrossWebModule acrossWebModule() {
		return new AcrossWebModule();
	}

	public static void main( String[] args ) {
		SpringApplication.run( SpringBootDataJpaApplication.class );
	}
}
