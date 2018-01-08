package ax.application.config;

import ax.application.domain.Domain;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Steven Gentens
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = Domain.class)
@EnableCaching
public class DomainConfiguration
{
}
