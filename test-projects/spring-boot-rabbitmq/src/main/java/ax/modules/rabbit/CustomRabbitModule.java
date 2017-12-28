package ax.modules.rabbit;

import com.foreach.across.core.AcrossModule;
import com.foreach.across.core.context.configurer.ApplicationContextConfigurer;
import com.foreach.across.core.context.configurer.ComponentScanConfigurer;

import java.util.Set;

/**
 * @author Steven Gentens
 */
public class CustomRabbitModule extends AcrossModule
{
	private final static String NAME = "customRabbitModule";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void registerDefaultApplicationContextConfigurers( Set<ApplicationContextConfigurer> contextConfigurers ) {
		contextConfigurers.add( ComponentScanConfigurer.forAcrossModule( CustomRabbitModule.class ) );
	}
}
