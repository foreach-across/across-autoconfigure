package ax.server.application.domain.book;

import com.foreach.across.core.annotations.ConditionalOnAcrossModule;
import com.foreach.across.modules.web.AcrossWebModule;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TestController for Spring Cloud Feign
 *
 * @author Gunther Van Geetsom
 * @since 1.0.1
 */
@ConditionalOnAcrossModule(AcrossWebModule.NAME)
@RestController
@RequiredArgsConstructor
public class BookController
{

	private final BookResource BOOK_1 = BookResource.builder()
	                                                .id( 1L )
	                                                .name( "Microservices eBook: Migrating to Cloud-Native Application Architectures" )
	                                                .author( "Matt Stine" )
	                                                .price( 49.99 )
	                                                .publishedOn( LocalDate.of( 2017, 10, 15 ) )
	                                                .build();

	private final BookResource BOOK_2 = BookResource.builder()
	                                                .id( 2L )
	                                                .name( "Cloud Foundry: The Cloud-Native Platform" )
	                                                .author( "Duncan Winn" )
	                                                .price( 25.50 )
	                                                .publishedOn( LocalDate.of( 2018, 5, 1 ) )
	                                                .build();

	private final ArrayList<BookResource> bookResources = new ArrayList<>();

	@PostConstruct
	public void init() {
		bookResources.add( BOOK_1 );
		bookResources.add( BOOK_2 );
	}

	@GetMapping("/books")
	public List<BookResource> showBooks() {
		return bookResources;
	}

	@GetMapping("/book/{id}")
	public ResponseEntity<BookResource> getOneBook( @PathVariable("id") Long id, @DateTimeFormat(pattern = "d/M/yy") @RequestParam LocalDate publishedAfter ) {
		Optional<BookResource> oBook = bookResources
				.stream()
				.filter( bookResource -> bookResource.getId() == id && bookResource.getPublishedOn().isAfter( publishedAfter ) )
				.findFirst();
		if ( oBook.isPresent() ) {
			return ResponseEntity.ok( oBook.get() );
		}
		return ResponseEntity.notFound().build();
	}
}
