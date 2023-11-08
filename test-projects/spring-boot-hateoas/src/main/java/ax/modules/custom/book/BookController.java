package ax.modules.custom.book;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.LinkRelationProvider;
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
	private final LinkRelationProvider relProvider;

	@GetMapping(value = "/{id}")
	public EntityModel<Book> book( @PathVariable(value = "id", required = false) String title ) {
		Book book = new Book( title );

		EntityModel<Book> bookResource = EntityModel.of( book );
		bookResource.add( entityLinks.linkToItemResource( Book.class, "someBook" ).withSelfRel() );
		bookResource.add( entityLinks.linkToItemResource( Book.class, "anotherBook" ).withRel( relProvider.getItemResourceRelFor( Book.class ) ) );
		return bookResource;
	}
}
