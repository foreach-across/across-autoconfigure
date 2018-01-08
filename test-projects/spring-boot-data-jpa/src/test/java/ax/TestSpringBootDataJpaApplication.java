package ax;

import ax.application.domain.book.Book;
import ax.application.domain.book.BookRepository;
import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Arne Vandamme
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = SpringBootDataJpaApplication.class)
public class TestSpringBootDataJpaApplication
{
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	protected AcrossContextBeanRegistry beanRegistry;

	@Test
	public void transactionManagerCreatedAndPropertiesSet() {
		Optional<AbstractPlatformTransactionManager> tm = beanRegistry.findBeanOfTypeFromModule( "SpringBootDataJpaApplicationModule",
		                                                                                         AbstractPlatformTransactionManager.class );

		assertTrue( tm.isPresent() );
		assertEquals( 25, tm.get().getDefaultTimeout() );
	}

	@Test
	public void savingAndFetchingBookIsPossible() {
		Book book = new Book();
		book.setAuthor( "John Doe" );
		book.setName( "John Doe's Biography" );
		book.setPrice( 100 );

		bookRepository.save( book );

		Book fetched = bookRepository.findOneByNameAndAuthor( "John Doe's Biography", "John Doe" );
		assertEquals( book, fetched );
	}

	@Test
	public void openEntityManagerInViewInterceptorRegistration() {
		assertTrue( beanRegistry.getBeansOfType( WebMvcConfigurer.class, true ).isEmpty() );
	}
}
