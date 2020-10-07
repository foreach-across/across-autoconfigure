package ax.application.repositories;

import ax.application.business.Book;
import com.foreach.across.core.annotations.Exposed;
import org.springframework.data.jpa.repository.JpaRepository;

@Exposed
public interface BookRepository extends JpaRepository<Book, Integer>
{
}
