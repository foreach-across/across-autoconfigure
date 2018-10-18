package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;

/**
 * @author Gunther Van Geetsom
 * @since 1.0.2
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
public class SpringBootHateoasApplication
{
	public static void main( String[] args ) {
		SpringApplication.run( SpringBootHateoasApplication.class );
	}
}
