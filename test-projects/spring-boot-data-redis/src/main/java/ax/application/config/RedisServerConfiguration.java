package ax.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

import java.io.IOException;

@Component
public class RedisServerConfiguration
{
	@Bean(destroyMethod = "stop")
	public RedisServer redisServer() throws IOException {
		RedisServer redisServer = new RedisServerBuilder().port( 6379 )
		                                                  // https://github.com/kstyrc/embedded-redis/issues/51
		                                                  // https://github.com/kstyrc/embedded-redis/issues/70
		                                                  .setting( "maxmemory 128M" )
		                                                  .build();
		redisServer.start();
		return redisServer;
	}
}
