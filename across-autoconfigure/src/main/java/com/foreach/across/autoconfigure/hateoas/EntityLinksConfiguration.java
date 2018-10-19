package com.foreach.across.autoconfigure.hateoas;

import com.foreach.across.autoconfigure.support.AcrossContextResolvingBeanTargetSource;
import com.foreach.across.core.context.bootstrap.ModuleBootstrapConfig;
import com.foreach.across.core.context.info.AcrossModuleInfo;
import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import com.foreach.across.core.events.AcrossModuleBootstrappedEvent;
import com.foreach.across.core.filters.BeanFilter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;

import static com.foreach.across.core.context.bootstrap.AcrossBootstrapConfigurer.CONTEXT_POSTPROCESSOR_MODULE;

/**
 * Insert lazy proxies for {@link EntityLinks} and {@link RelProvider}.
 * Actual instances will be resolved from the {@code AcrossContextPostProcessorModule} upon first use.
 * The regular hypermedia configuration should be injected in the postprocessor module.
 *
 * @author Arne Vandamme
 * @see HypermediaAutoConfigurationAdapter
 * @since 1.0.2
 */
@Configuration
@RequiredArgsConstructor
class EntityLinksConfiguration
{
	private final AcrossModuleInfo currentModule;

	@Bean
	@ConditionalOnMissingBean(RelProvider.class)
	@Primary
	public RelProvider internalRelProvider( AcrossContextBeanRegistry beanRegistry ) {
		AcrossContextResolvingBeanTargetSource targetSource = new AcrossContextResolvingBeanTargetSource();
		targetSource.setBeanRegistry( beanRegistry );
		targetSource.setModuleName( CONTEXT_POSTPROCESSOR_MODULE );
		targetSource.setBeanType( RelProvider.class );
		return ProxyFactory.getProxy( RelProvider.class, targetSource );
	}

	@Bean
	@ConditionalOnMissingBean(EntityLinks.class)
	@Primary
	public EntityLinks internalEntityLinks( AcrossContextBeanRegistry beanRegistry ) {
		AcrossContextResolvingBeanTargetSource targetSource = new AcrossContextResolvingBeanTargetSource();
		targetSource.setBeanRegistry( beanRegistry );
		targetSource.setModuleName( CONTEXT_POSTPROCESSOR_MODULE );
		targetSource.setBeanType( EntityLinks.class );
		return ProxyFactory.getProxy( EntityLinks.class, targetSource );
	}

	/**
	 * We do not want to expose our custom proxies as they would then be picked up again by the postprocessor
	 * as candidates for rel or link building. This would cause an infinite loop.
	 * <p>
	 * Because Across (currently) has no way to forcibly not-expose a bean, we need to add an expose filter that
	 * excludes them explicitly. The only way to do that is to wrap the configured expose filter as late as possible
	 * and exclude those beans. This way we can avoid the configured filter to be used for those beans (as exposing
	 * happens as soon as a single filter returns {@code true}.
	 * <p>
	 * The last possible time to update the expose filter is after the module itself has bootstrapped.
	 * Beans have not yet been exposed at that point so the filter can still be modified.
	 */
	@EventListener
	@Order
	public void suppressInternalBeans( AcrossModuleBootstrappedEvent moduleBootstrappedEvent ) {
		if ( StringUtils.equals( currentModule.getName(), moduleBootstrappedEvent.getModule().getName() ) ) {
			ModuleBootstrapConfig bootstrapConfiguration = moduleBootstrappedEvent.getModule().getBootstrapConfiguration();
			bootstrapConfiguration.setExposeFilter( hateaosInfrastructureBeans( bootstrapConfiguration.getExposeFilter() ) );
		}
	}

	private BeanFilter hateaosInfrastructureBeans( BeanFilter originalExposeFilter ) {
		return ( beanFactory, beanName, bean, definition ) -> {
			if ( StringUtils.equals( "internalRelProvider", beanName ) || StringUtils.equals( "internalEntityLinks", beanName ) ) {
				return false;
			}
			return originalExposeFilter.apply( beanFactory, beanName, bean, definition );
		};
	}
}
