package ax.application;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyHealthIndicator implements HealthIndicator
{
	private final AtomicInteger counter = new AtomicInteger();

	public Health health() {
		return Health.down().withDetail( "count", counter.incrementAndGet() ).build();
	}
}
