package ax.client.application;

import com.foreach.across.core.annotations.Exposed;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@Component
@Exposed
@ConfigurationProperties("random")
public class RandomSettings
{
	@Getter
	private String property;
}
