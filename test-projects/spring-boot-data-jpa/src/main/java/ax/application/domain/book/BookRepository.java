package ax.application.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author Arne Vandamme
 */
public interface BookRepository extends JpaRepository<Book, Long>, QueryDslPredicateExecutor<Book>
{
	Book findOneByNameAndAuthor( String name, String author );
}
