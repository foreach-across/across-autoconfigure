package ax.application.resolver;

import ax.application.domain.article.Article;
import ax.application.domain.article.ArticleRepository;
import ax.application.domain.article.input.CreateArticleInput;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Component
public class MutationResolver implements GraphQLMutationResolver
{
	private final ArticleRepository articleRepository;

	@Transactional
	public Article createArticle( CreateArticleInput input ) {
		return articleRepository.saveAndFlush( new Article( null, input.getTitle(), input.getText(), input.getAuthorId() ) );
	}

}
