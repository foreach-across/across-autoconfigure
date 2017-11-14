package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.applicationinfo.ApplicationInfoModule;
import com.foreach.across.modules.debugweb.DebugWebModule;
import com.foreach.across.modules.web.AcrossWebModule;
import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;

/**
 * @author Marc Vanbrabant
 */
@AcrossApplication(
		modules = { AcrossWebModule.NAME, DebugWebModule.NAME, ApplicationInfoModule.NAME }
)
@EnableAdminServer
public class SpringBootAdminApplication
{
	public static void main( String[] args ) {
		SpringApplication springApplication = new SpringApplication( SpringBootAdminApplication.class );
		springApplication.run( args );
	}
}
