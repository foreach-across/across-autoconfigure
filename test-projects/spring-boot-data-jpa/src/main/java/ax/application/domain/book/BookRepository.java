package ax.application.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Arne Vandamme
 */
public interface BookRepository extends JpaRepository<Book, Long>
{
	Book findOneByNameAndAuthor( String name, String author );
}
