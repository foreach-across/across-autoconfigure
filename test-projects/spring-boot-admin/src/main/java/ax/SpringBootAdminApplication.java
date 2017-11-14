package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import de.codecentric.boot.admin.client.config.SpringBootAdminClientAutoConfiguration;
import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementServerPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author Marc Vanbrabant
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
@EnableAdminServer
public class SpringBootAdminApplication
{
	public static void main( String[] args ) {
		SpringApplication springApplication = new SpringApplication( SpringBootAdminApplication.class );
		springApplication.run( args );
	}
}
