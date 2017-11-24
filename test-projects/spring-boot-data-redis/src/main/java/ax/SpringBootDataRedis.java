package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@AcrossApplication(
        modules = AcrossWebModule.NAME
)
public class SpringBootDataRedis {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataRedis.class);
    }

    @Bean(destroyMethod = "stop")
    public RedisServer redisServer() throws IOException {
        RedisServer redisServer = new RedisServer(6379);
        redisServer.start();
        return redisServer;
    }
}
