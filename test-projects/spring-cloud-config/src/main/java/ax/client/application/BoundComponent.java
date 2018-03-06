package ax.client.application;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@Component
@RefreshScope
public class BoundComponent
{
	@Value("${fixed.message}")
	@Getter
	private String fixedMessage;

	@Value("${custom.property:}")
	@Getter
	private String customProperty;
}
