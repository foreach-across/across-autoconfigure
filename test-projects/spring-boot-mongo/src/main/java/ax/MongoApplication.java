package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;

/**
 * @author Marc Vanbrabant
 * @since 1.0.0
 */
@AcrossApplication(
        modules = AcrossWebModule.NAME
)
public class MongoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MongoApplication.class);
    }
}
