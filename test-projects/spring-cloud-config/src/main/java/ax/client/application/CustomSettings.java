package ax.client.application;

import com.foreach.across.core.annotations.Exposed;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@Component
@Exposed
@ConfigurationProperties("custom")
@Data
public class CustomSettings
{
	private String property;
}
