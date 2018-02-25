package ax.application.domain.book;

import com.foreach.across.core.annotations.ConditionalOnAcrossModule;
import com.foreach.across.modules.web.AcrossWebModule;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Test controller for spring data web support.
 *
 * @author Arne Vandamme
 * @since 1.0.0
 */
@ConditionalOnAcrossModule(AcrossWebModule.NAME)
@RestController
@RequiredArgsConstructor
public class BookController
{
	private final BookRepository bookRepository;

	@GetMapping("/books")
	public String showBooks( Sort sort ) {
		return bookRepository.findAll( sort )
		                     .stream()
		                     .map( Book::getName )
		                     .collect( Collectors.joining( "," ) );
	}

	@GetMapping("/find")
	public String findBooks( @QuerydslPredicate(root = Book.class) Predicate predicate ) {
		return ( (List<Book>) bookRepository.findAll( predicate ) )
				.stream()
				.map( Book::getName )
				.collect( Collectors.joining( "," ) );
	}

	@GetMapping("/book/{id}")
	public String book( @PathVariable("id") Book book ) {
		return book.getName();
	}
}
