package ax.modules.custom.greeting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 * @author Arne Vandamme
 * @since 1.0.2
 */
public class Greeting extends ResourceSupport
{
	private final String content;

	@JsonCreator
	public Greeting( @JsonProperty("content") String content ) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}