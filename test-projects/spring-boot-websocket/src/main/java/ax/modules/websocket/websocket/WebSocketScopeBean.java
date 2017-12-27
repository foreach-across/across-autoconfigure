package ax.modules.websocket.websocket;

import com.foreach.across.core.annotations.Exposed;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author Steven Gentens
 */
@Component
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Exposed
public class WebSocketScopeBean
{
	private String uuid;

	@PostConstruct
	public void init() {
		uuid = UUID.randomUUID().toString();
	}

	public String getUuid() {
		return uuid;
	}
}
