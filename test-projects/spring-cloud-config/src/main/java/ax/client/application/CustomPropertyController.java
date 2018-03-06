package ax.client.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@RestController
@RefreshScope
public class CustomPropertyController
{
	@Value("${custom.property:}")
	private String customProperty;

	@GetMapping("/applicationModule/customProperty")
	public String renderCustomPropertyValue() {
		return customProperty;
	}
}
