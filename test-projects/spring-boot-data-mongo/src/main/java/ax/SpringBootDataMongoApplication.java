package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;

/**
 * @author Marc Vanbrabant
 * @since 1.0.0
 */
@AcrossApplication(
        modules = AcrossWebModule.NAME
)
public class SpringBootDataMongoApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootDataMongoApplication.class).contextClass(Me.class).build().run();
    }

    public static class Me extends AnnotationConfigEmbeddedWebApplicationContext {
        public Me() {
        }

        public Me(Class<?>... annotatedClasses) {
            super(annotatedClasses);
        }

        public Me(String... basePackages) {
            super(basePackages);
        }
    }
}
