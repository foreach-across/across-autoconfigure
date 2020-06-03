package com.foreach.across.autoconfigure.hateoas;

import com.foreach.across.core.context.bootstrap.AcrossBootstrapConfig;
import com.foreach.across.core.context.bootstrap.AcrossBootstrapConfigurer;
import com.foreach.across.core.context.bootstrap.ModuleBootstrapConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Resource;
import org.springframework.plugin.core.Plugin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Adapts the default {@link org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration}.
 * Ensures that {@code EntityLinks} and {@code RelProvider} are available in all Across modules.
 *
 * @author Arne Vandamme
 * @since 1.0.2
 */
@Configuration
@ConditionalOnClass({ Resource.class, RequestMapping.class, Plugin.class })
@ConditionalOnWebApplication
public class HypermediaAutoConfigurationAdapter implements AcrossBootstrapConfigurer
{
	@Override
	public void configureContext( AcrossBootstrapConfig contextConfiguration ) {
		contextConfiguration.extendModule( CONTEXT_POSTPROCESSOR_MODULE, HypermediaAutoConfiguration.class.getName() );
	}

	@Override
	public void configureModule( ModuleBootstrapConfig moduleConfiguration ) {
		if ( !CONTEXT_POSTPROCESSOR_MODULE.equals( moduleConfiguration.getModuleName() ) ) {
			moduleConfiguration.extendModule( true, true, EntityLinksConfiguration.class.getName() );
		}
	}
}
