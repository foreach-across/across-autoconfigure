package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;

/**
 * @author Steven Gentens
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
public class SpringBootSocialLinkedinApplication
{
	public static void main( String[] args ) {
		SpringApplication.run( SpringBootSocialLinkedinApplication.class );
	}
}