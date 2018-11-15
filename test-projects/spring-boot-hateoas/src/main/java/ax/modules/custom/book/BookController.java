package ax.modules.custom.book;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ExposesResourceFor(Book.class)
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController
{
	private final EntityLinks entityLinks;
	private final RelProvider relProvider;

	@GetMapping(value = "/{id}")
	public Resource<Book> book( @PathVariable(value = "id", required = false) String title ) {
		Book book = new Book( title );

		Resource<Book> bookResource = new Resource<>( book );
		bookResource.add( entityLinks.linkToSingleResource( Book.class, "someBook" ).withSelfRel() );
		bookResource.add( entityLinks.linkToSingleResource( Book.class, "anotherBook" ).withRel( relProvider.getItemResourceRelFor( Book.class ) ) );
		return bookResource;
	}
}
