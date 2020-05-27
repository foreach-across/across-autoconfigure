package ax;

import ax.application.business.Book;
import ax.application.repositories.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.hateoas.client.Hop.rel;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootDataRestApplication.class)
public class TestSpringBootDataRestApplication
{
	@Value("${local.server.port}")
	private int port;
	@Autowired
	private BookRepository repository;

	@Test
	public void shouldBootstrap() throws Exception {
		assertNotNull( repository );
		assertEquals( 0, repository.count() );

		assertEquals( 0, getBooks().size() );

		Book book = new Book();
		book.setTitle( "Gutenberg Bible" );
		book.setDescription( "The Gutenberg Bible is the first substantial book printed in the West with moveable metal type." );
		repository.save( book );

		assertEquals( 1, getBooks().size() );
	}

	private Collection<Book> getBooks() throws URISyntaxException {
		final Traverson traverson = new Traverson( getBaseUri( "/api" ), MediaTypes.HAL_JSON );
		final ParameterizedTypeReference<PagedModel<Book>> resourceParameterizedTypeReference = new ParameterizedTypeReference<PagedModel<Book>>()
		{
		};
		final PagedModel<Book> resources = traverson.follow( rel( "books" ) ).toObject( resourceParameterizedTypeReference );
		return resources.getContent();
	}

	private URI getBaseUri( String path ) throws URISyntaxException {
		return new URI( "http://localhost:" + port + path );
	}
}
