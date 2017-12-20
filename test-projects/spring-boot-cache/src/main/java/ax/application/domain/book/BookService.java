package ax.application.domain.book;

import java.util.List;

/**
 * @author Steven Gentens
 */
public interface BookService
{
	List<Book> findAll();
	void saveBook(Book book);
	Book findOne(long id);
	void delete(long id);
	List<Book> findByName(String name);
	List<Book> findByNameAndAuthor(String name, String author);
	List<Book> findByPrice(long price);
}
