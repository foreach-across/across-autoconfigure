package ax.modules.custom;

import com.foreach.across.core.AcrossModule;
import com.foreach.across.core.context.configurer.ApplicationContextConfigurer;
import com.foreach.across.core.context.configurer.ComponentScanConfigurer;

import java.util.Set;

/**
 * Custom EntityLinks consuming module, asserting that links are available in all modules.
 *
 * @author Arne Vandamme
 * @since 1.0.2
 */
public class CustomModule extends AcrossModule
{
	@Override
	public String getName() {
		return "CustomModule";
	}

	@Override
	protected void registerDefaultApplicationContextConfigurers( Set<ApplicationContextConfigurer> contextConfigurers ) {
		contextConfigurers.add( ComponentScanConfigurer.forAcrossModule( getClass() ) );
	}
}
