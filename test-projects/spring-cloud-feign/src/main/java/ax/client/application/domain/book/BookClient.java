package ax.client.application.domain.book;

import com.foreach.across.core.annotations.Exposed;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * Feign client
 *
 * @author Gunther Van Geetsom
 * @since 1.0.1
 */
@FeignClient(name = "BookClient", url = "${book.api.url}", fallback = BookClientFallback.class)
@Exposed
public interface BookClient
{
	@GetMapping("/books")
	List<BookClientResource> getBooks();

	@GetMapping("/book/{id}")
	BookClientResource getOneBook( @PathVariable("id") Long id, @RequestParam("publishedAfter") LocalDate publishedAfter );
}
