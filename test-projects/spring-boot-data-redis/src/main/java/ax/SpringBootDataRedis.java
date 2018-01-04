package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

import java.io.IOException;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
public class SpringBootDataRedis
{
	public static void main( String[] args ) {
		SpringApplication.run( SpringBootDataRedis.class );
	}

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
