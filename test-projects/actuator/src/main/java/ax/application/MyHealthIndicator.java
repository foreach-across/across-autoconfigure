package ax.application;

import com.foreach.across.core.annotations.Exposed;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Exposed
public class MyHealthIndicator implements HealthIndicator
{
	private final AtomicInteger counter = new AtomicInteger();

	public Health health() {
		return Health.down().withDetail( "count", counter.incrementAndGet() ).build();
	}
}
