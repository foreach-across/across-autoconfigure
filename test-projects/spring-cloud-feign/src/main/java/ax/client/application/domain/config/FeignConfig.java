package ax.client.application.domain.config;

import ax.client.application.domain.book.BookClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gunther Van Geetsom
 * @since 1.0.1
 */
@EnableFeignClients(basePackageClasses = BookClient.class)
@Configuration
public class FeignConfig
{
}
