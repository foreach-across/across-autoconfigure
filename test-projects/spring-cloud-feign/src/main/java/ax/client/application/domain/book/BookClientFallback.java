package ax.client.application.domain.book;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Hystrix Fallback
 *
 * @author Gunther Van Geetsom
 * @since 1.0.1
 */
@Component
public class BookClientFallback implements BookClient
{
	@Override
	public List<BookClientResource> getBooks() {
		return new ArrayList<>();
	}

	@Override
	public BookClientResource getOneBook( Long id, LocalDate publishedAfter ) {
		return BookClientResource.builder().id( -99 ).build();
	}
}
