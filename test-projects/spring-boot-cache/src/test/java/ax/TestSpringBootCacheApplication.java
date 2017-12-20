package ax;

import ax.application.domain.book.Book;
import ax.application.domain.book.BookService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * @author Steven Gentens
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootCacheApplication.class)
public class TestSpringBootCacheApplication
{
	@Autowired
	private BookService bookService;

	@Autowired
	private CacheManager cacheManager;

	@Before
	public void setUp(){
		assertNotNull( bookService );
		Book book = new Book();
		book.setId( 1L );
		book.setAuthor( "John" );
		book.setName( "Winter Wonderland" );
		book.setPrice( 123L );
		bookService.saveBook( book );

	}

	@Test
	@Ignore("Across provides a NoOpCacheManager by default")
	public void shouldBootstrap(){
		assertNotNull( cacheManager );
		Book book = bookService.findOne( 1L );
		Cache cache = cacheManager.getCache( "books" );
		Object nativeCache = cache.getNativeCache();
		assertNotNull( nativeCache );
	}
}
