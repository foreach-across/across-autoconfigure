package ax.application.extensions;

import ax.application.domain.Domain;
import com.foreach.across.core.annotations.ModuleConfiguration;
import com.foreach.across.modules.hibernate.jpa.AcrossHibernateJpaModule;
import com.foreach.across.modules.hibernate.provider.HibernatePackageConfigurer;
import com.foreach.across.modules.hibernate.provider.HibernatePackageRegistry;

/**
 * @author Steven Gentens
 */
@ModuleConfiguration(AcrossHibernateJpaModule.NAME)
public class EntityScanConfiguration implements HibernatePackageConfigurer
{
	@Override
	public void configureHibernatePackage( HibernatePackageRegistry hibernatePackageRegistry ) {
		hibernatePackageRegistry.addPackageToScan( Domain.class );
	}
}
