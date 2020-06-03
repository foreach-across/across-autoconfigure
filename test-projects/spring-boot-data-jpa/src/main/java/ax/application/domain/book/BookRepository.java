package ax.application.domain.book;

import com.foreach.across.core.annotations.Exposed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Arne Vandamme
 */
@Exposed
@RepositoryRestResource(collectionResourceRel = "books", path = "books")
public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book>
{
	Book findOneByNameAndAuthor( String name, String author );
}
