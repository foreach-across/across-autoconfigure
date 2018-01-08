package ax;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SpringBootDataJpaApplication.class)
public class TestSpringBootDataJpaWebApplication extends TestSpringBootDataJpaApplication
{
	@Override
	public void openEntityManagerInViewInterceptorRegistration() {
		Set<String> beanNames = beanRegistry.getBeansOfTypeAsMap( WebMvcConfigurer.class, true ).keySet();
		assertTrue( beanNames.stream().anyMatch( beanName -> beanName.contains( "JpaWebMvcConfiguration" ) ) );
	}
}
