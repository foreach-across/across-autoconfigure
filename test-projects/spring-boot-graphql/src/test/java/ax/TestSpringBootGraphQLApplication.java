package ax;

import ax.application.domain.article.Article;
import ax.application.domain.article.ArticleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { SpringBootGraphQlApplication.class })
public class TestSpringBootGraphQLApplication
{
	@Value("${graphql.servlet.mapping}")
	private String graphQLEndpoint;

	@Value("${graphiql.mapping}")
	private String graphiQLEndpoint;

	@Value("${voyager.mapping}")
	private String voyagerEndpoint;

	@Value("${local.server.port}")
	private int port;

	private RestTemplate restTemplate;
	private String graphQLUrl;
	private String graphiQLUrl;
	private String voyagerUrl;

	@Autowired
	private ArticleRepository articleRepository;

	@Before
	public void init() {
		restTemplate = new RestTemplate();
		UriComponentsBuilder basePath = UriComponentsBuilder.newInstance().host( "localhost" )
		                                                    .scheme( "http" )
		                                                    .port( this.port );
		graphQLUrl = basePath.cloneBuilder().path( graphQLEndpoint ).toUriString();
		graphiQLUrl = basePath.cloneBuilder().path( graphiQLEndpoint ).toUriString();
		voyagerUrl = basePath.cloneBuilder().path( voyagerEndpoint ).toUriString();
	}

	@Test
	public void getArticles() {
		String query = "query AllArticles { articles { id title author { id username } } }";
		assertEquals(
				"{\"data\":{\"articles\":[{\"id\":1,\"title\":\"Hello wold\",\"author\":{\"id\":1,\"username\":\"g00glen00b\"}},{\"id\":2,\"title\":\"Foo\",\"author\":{\"id\":2,\"username\":\"admin\"}}]}}",
				restTemplate.postForObject( graphQLUrl, new QueryObject( query, "", Collections.emptyMap() ), String.class ) );
	}

	@Test
	public void addArticle() {
		assertEquals( 2, articleRepository.count() );
		String query = "mutation CreateArticle ( $input: CreateArticleInput!) { createArticle (input: $input) {id title author {id username}}}";
		Map<String, Object> input = new HashMap<>();
		String title = "Writing GraphQL mutations with Spring boot";
		input.put( "title", title );
		input.put( "text", "This is the text for our blog article." );
		input.put( "authorId", 1 );
		assertEquals(
				"{\"data\":{\"createArticle\":{\"id\":3,\"title\":\"Writing GraphQL mutations with Spring boot\",\"author\":{\"id\":1,\"username\":\"g00glen00b\"}}}}",
				restTemplate.postForObject( graphQLUrl, new QueryObject( query, "", Collections.singletonMap( "input", input ) ), String.class ) );
		assertEquals( 3, articleRepository.count() );
		Article created = articleRepository.findAll().stream().max( Comparator.comparing( Article::getId ) ).get();
		assertEquals( title, created.getTitle() );
		articleRepository.delete( created.getId() );
	}

	@Test
	public void graphiQlEndpointIsAvailable() {
		assertEquals( HttpStatus.OK, restTemplate.getForEntity( graphiQLUrl, String.class ).getStatusCode() );
	}

	@Test
	public void voyagerEndpointIsAvailable() {
		assertEquals( HttpStatus.OK, restTemplate.getForEntity( voyagerUrl, String.class ).getStatusCode() );
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class QueryObject
	{
		private String query;
		private String operationName;
		private Map<String, Object> variables;
	}

}
