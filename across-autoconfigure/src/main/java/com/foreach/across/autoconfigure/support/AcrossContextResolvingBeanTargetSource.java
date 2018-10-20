package com.foreach.across.autoconfigure.support;

import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.aop.target.AbstractLazyCreationTargetSource;

/**
 * Resolves the actual target bean from the Across context upon first use.
 * Depending on the property values will resolve the target bean by name or type.
 * If a module name is specified it will retrieve the target from that module
 * specifically, else will attempt to resolve in the collection of exposed beans.
 *
 * @author Arne Vandamme
 * @since 1.0.2
 */
@RequiredArgsConstructor
@Setter
public class AcrossContextResolvingBeanTargetSource extends AbstractLazyCreationTargetSource
{
	private AcrossContextBeanRegistry beanRegistry;
	private String moduleName;
	private Class<?> beanType;
	private String beanName;

	@Override
	protected Object createObject() {
		if ( moduleName != null ) {
			if ( beanName != null ) {
				return beanRegistry.getBeanFromModule( moduleName, beanName );
			}
			return beanRegistry.getBeanOfTypeFromModule( moduleName, beanType );
		}

		if ( beanName != null ) {
			return beanRegistry.getBean( beanName );
		}

		return beanRegistry.getBeanOfType( beanType );
	}
}
