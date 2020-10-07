package ax;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SpringBootDataJpaApplication.class)
public class TestSpringBootDataJpaWebApplication extends TestSpringBootDataJpaApplication
{
	@Override
	public void openEntityManagerInViewInterceptorRegistration() {
		Set<String> beanNames = beanRegistry.getBeansOfTypeAsMap( WebMvcConfigurer.class, true ).keySet();
		assertTrue( beanNames.stream().anyMatch( beanName -> beanName.contains( "openEntityManagerInViewInterceptorConfigurer" ) ) );
	}
}
