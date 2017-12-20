package ax.application.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Steven Gentens
 */
public interface BookRepository extends JpaRepository<Book, Long>
{
	List<Book> findByName( String name );

	List<Book> findByPrice( long price );

	List<Book> findByNameAndAuthor( String name, String author );
}
