package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * @author Marc Vanbrabant
 * @since 1.0.0
 */
@AcrossApplication(
        modules = AcrossWebModule.NAME
)
public class SprintBootMailApplication {
    @Bean(destroyMethod = "stop")
    public GreenMail simpleSmtpServer() throws IOException {
        GreenMail greenMail = new GreenMail(new ServerSetup(64215, null, ServerSetup.PROTOCOL_SMTP));
        greenMail.start();
        return greenMail;
    }

    public static void main(String[] args) {
        SpringApplication.run(SprintBootMailApplication.class);
    }
}
