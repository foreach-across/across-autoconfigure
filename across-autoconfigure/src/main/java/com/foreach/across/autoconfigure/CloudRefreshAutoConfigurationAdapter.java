package com.foreach.across.autoconfigure;

import com.foreach.across.config.AcrossContextConfigurer;
import com.foreach.across.core.AcrossContext;
import com.foreach.across.core.context.bootstrap.AcrossBootstrapConfigurer;
import com.foreach.across.core.context.bootstrap.ModuleBootstrapConfig;
import com.foreach.across.core.context.configurer.AnnotatedClassConfigurer;
import com.foreach.across.core.context.configurer.ConfigurerScope;
import com.foreach.across.core.context.info.AcrossContextInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Adapts Spring Cloud refresh support for an Across application.
 * This class injects support for the {@link RefreshScope} in the Across context itself,
 * as well as in every Across module. It also acts as an application listener for the
 * {@link EnvironmentChangeEvent} and implements custom code for refreshing the Across
 * context beans and updating the module environments when a change event happens.
 *
 * @author Arne Vandamme
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({ RefreshAutoConfiguration.class, RefreshScope.class })
@Import(RefreshAutoConfiguration.class)
public class CloudRefreshAutoConfigurationAdapter implements AcrossContextConfigurer, ApplicationContextAware, AcrossBootstrapConfigurer, ApplicationListener<EnvironmentChangeEvent>
{
	private static final Set<String> STANDARD_SOURCES = new HashSet<>(
			Arrays.asList( StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME,
			               StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
			               StandardServletEnvironment.JNDI_PROPERTY_SOURCE_NAME,
			               StandardServletEnvironment.SERVLET_CONFIG_PROPERTY_SOURCE_NAME,
			               StandardServletEnvironment.SERVLET_CONTEXT_PROPERTY_SOURCE_NAME ) );

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void configure( AcrossContext context ) {
		context.addApplicationContextConfigurer(
				new AnnotatedClassConfigurer( RefreshScopeConfiguration.class, ConfigurationPropertiesRebinderAutoConfiguration.class ),
				ConfigurerScope.CONTEXT_ONLY
		);
	}

	@Override
	public void configureModule( ModuleBootstrapConfig moduleConfiguration ) {
		moduleConfiguration.extendModule( true, true, RefreshScopeConfiguration.class.getName(),
		                                  ConfigurationPropertiesRebinderAutoConfiguration.class.getName() );
	}

	@Override
	public void onApplicationEvent( EnvironmentChangeEvent event ) {
		AcrossContextInfo contextInfo = applicationContext.getBean( AcrossContextInfo.class );

		// Refresh the Across context and all Across module contexts separately
		ConfigurableEnvironment changedEnvironment = (ConfigurableEnvironment) applicationContext.getEnvironment();
		ConfigurableEnvironment contextEnvironment = refresh( contextInfo.getApplicationContext(), changedEnvironment );
		contextInfo.getModules().forEach( module -> refresh( module.getApplicationContext(), contextEnvironment ) );
	}

	private ConfigurableEnvironment refresh( ApplicationContext applicationContext, ConfigurableEnvironment updatedEnvironment ) {
		ConfigurableEnvironment target = (ConfigurableEnvironment) applicationContext.getEnvironment();
		mergeEnvironments( target, updatedEnvironment );
		applicationContext.getBean( RefreshScope.class ).refreshAll();
		applicationContext.getBean( ConfigurationPropertiesRebinder.class ).rebind();
		return target;
	}

	/**
	 * Code duplicated from {@link org.springframework.cloud.context.refresh.ContextRefresher}. Environment merge does
	 * not have the desired effect so manual updating of the different property sources is done.
	 */
	private void mergeEnvironments( ConfigurableEnvironment targetEnvironment, ConfigurableEnvironment updatedEnvironment ) {
		MutablePropertySources target = targetEnvironment.getPropertySources();
		String targetName = null;
		for ( PropertySource<?> source : updatedEnvironment.getPropertySources() ) {
			String name = source.getName();
			if ( target.contains( name ) ) {
				targetName = name;
			}
			if ( !STANDARD_SOURCES.contains( name ) ) {
				if ( target.contains( name ) ) {
					target.replace( name, source );
				}
				else {
					if ( targetName != null ) {
						target.addAfter( targetName, source );
					}
					else {
						// targetName was null so we are at the start of the list
						target.addFirst( source );
						targetName = name;
					}
				}
			}
		}
	}

	@ConditionalOnMissingBean(value = RefreshScope.class, search = SearchStrategy.CURRENT)
	protected static class RefreshScopeConfiguration implements BeanDefinitionRegistryPostProcessor
	{
		@Override
		public void postProcessBeanFactory( ConfigurableListableBeanFactory beanFactory )
				throws BeansException {
		}

		@Override
		public void postProcessBeanDefinitionRegistry( BeanDefinitionRegistry registry )
				throws BeansException {
			if ( !registry.containsBeanDefinition( "refreshScope" ) ) {
				// double check in case conditional is being skipped
				registry.registerBeanDefinition( "refreshScope",
				                                 BeanDefinitionBuilder.genericBeanDefinition( RefreshScope.class )
				                                                      .setRole( BeanDefinition.ROLE_INFRASTRUCTURE )
				                                                      .getBeanDefinition() );
			}
		}
	}
}
