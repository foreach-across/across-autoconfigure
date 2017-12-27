package ax.modules.websocket;

import com.foreach.across.core.AcrossModule;
import com.foreach.across.core.context.configurer.ApplicationContextConfigurer;
import com.foreach.across.core.context.configurer.ComponentScanConfigurer;

import java.util.Set;

/**
 * @author Steven Gentens
 */
public class CustomWebsocketModule extends AcrossModule
{
	public static final String NAME = "CustomWebsocketModule";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void registerDefaultApplicationContextConfigurers( Set<ApplicationContextConfigurer> contextConfigurers ) {
		contextConfigurers.add( ComponentScanConfigurer.forAcrossModule( CustomWebsocketModule.class ) );
	}
}
