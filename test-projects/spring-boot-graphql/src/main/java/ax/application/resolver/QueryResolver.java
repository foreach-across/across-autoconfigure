package ax.application.resolver;

import ax.application.domain.article.Article;
import ax.application.domain.article.ArticleRepository;
import ax.application.domain.comment.Comment;
import ax.application.domain.comment.CommentRepository;
import ax.application.domain.profile.Profile;
import ax.application.domain.profile.ProfileRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class QueryResolver implements GraphQLQueryResolver
{
	private ArticleRepository articleRepository;
	private CommentRepository commentRepository;
	private ProfileRepository profileRepository;

	public List<Article> getArticles() {
		return articleRepository.findAll();
	}

	public List<Comment> getComments() {
		return commentRepository.findAll();
	}

	public List<Profile> getProfiles() {
		return profileRepository.findAll();
	}

	public Article getArticle( Long id ) {
		return articleRepository.findOne( id );
	}
}
