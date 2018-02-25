package ax;

import ax.application.domain.book.Book;
import ax.application.domain.book.BookRepository;
import com.foreach.across.test.support.config.MockMvcConfiguration;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = { SpringBootDataJpaApplication.class, MockMvcConfiguration.class })
@ActiveProfiles("across-web")
public class TestSpringBootDataJpaAcrossWebApplication extends TestSpringBootDataJpaWebApplication
{
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private MockMvc mockMvc;

	@Override
	public void openEntityManagerInViewInterceptorRegistration() {
		Set<String> beanNames = beanRegistry.getBeansOfTypeAsMap( WebMvcConfigurer.class, true ).keySet();
		assertTrue( beanNames.stream().anyMatch( beanName -> beanName.contains( "JpaWebMvcConfiguration" ) ) );
	}

	@Test
	@SneakyThrows
	public void springDataWebSupportShouldBeActive() {
		bookRepository.deleteAll();

		Book bookA = Book.builder().author( "JohnDoe" ).name( "book a" ).build();
		bookRepository.save( bookA );
		Book bookB = Book.builder().author( "JaneDoe" ).name( "book b" ).build();
		bookRepository.save( bookB );

		mockMvc.perform( get( "/books" ) )
		       .andExpect( status().isOk() )
		       .andExpect( content().string( "book a,book b" ) );

		mockMvc.perform( get( "/books?sort=name,DESC" ) )
		       .andExpect( status().isOk() )
		       .andExpect( content().string( "book b,book a" ) );

		mockMvc.perform( get( "/find?author=JohnDoe" ) )
		       .andExpect( status().isOk() )
		       .andExpect( content().string( "book a" ) );

		mockMvc.perform( get( "/book/" + bookA.getId() ) )
		       .andExpect( status().isOk() )
		       .andExpect( content().string( "book a" ) );
		mockMvc.perform( get( "/book/" + bookB.getId() ) )
		       .andExpect( status().isOk() )
		       .andExpect( content().string( "book b" ) );
	}
}
