package ax;

import ax.application.domain.book.Book;
import ax.application.domain.book.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCache;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Steven Gentens
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootCacheApplication.class)
public class TestSpringBootCacheApplication
{
	@Autowired
	private BookService bookService;

	@Autowired
	private CacheManager cacheManager;

	@BeforeEach
	public void setUp() {
		assertNotNull( bookService );
		Book book = new Book();
		book.setId( 1L );
		book.setAuthor( "John" );
		book.setName( "Winter Wonderland" );
		book.setPrice( 123L );
		bookService.saveBook( book );

	}

	@Test
	public void shouldBootstrap() {
		assertNotNull( cacheManager );
		Book book = bookService.findOne( 1L );
		Cache cache = cacheManager.getCache( "books" );
		assertTrue( cache instanceof NoOpCache, "Across configures its own cache via AcrossCompositeCacheManager" );
		Object nativeCache = cache.getNativeCache();
		assertNotNull( nativeCache );
	}
}
