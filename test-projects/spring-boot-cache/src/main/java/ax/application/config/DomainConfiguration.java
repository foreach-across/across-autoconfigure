package ax.application.config;

import ax.application.domain.Domain;
import com.foreach.across.modules.hibernate.jpa.repositories.config.EnableAcrossJpaRepositories;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @author Steven Gentens
 */
@Configuration
@EnableAcrossJpaRepositories(basePackageClasses = Domain.class)
@EnableCaching
public class DomainConfiguration
{
}
