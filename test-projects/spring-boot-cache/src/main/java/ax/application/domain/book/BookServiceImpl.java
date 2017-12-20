package ax.application.domain.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Steven Gentens
 */
@Service
public class BookServiceImpl implements BookService
{
	@Autowired
	private BookRepository bookRepository;

	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	public List<Book> findByName( String name ) {
		return bookRepository.findByName( name );
	}

	public List<Book> findByPrice( long price ) {
		return bookRepository.findByPrice( price );
	}

	public List<Book> findByNameAndAuthor( String name, String author ) {
		return bookRepository.findByNameAndAuthor( name, author );
	}

	public void saveBook( Book book ) {
		bookRepository.save( book );
	}

	@org.springframework.cache.annotation.Cacheable(value = "books", key = "#id")
	public Book findOne( long id ) {
		return bookRepository.findOne( id );
	}

	public void delete( long id ) {
		bookRepository.delete( id );
	}
}
