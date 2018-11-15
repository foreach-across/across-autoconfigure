package ax.application.domain.article;

import ax.application.domain.comment.Comment;
import ax.application.domain.comment.CommentRepository;
import ax.application.domain.profile.Profile;
import ax.application.domain.profile.ProfileRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ArticleResolver implements GraphQLResolver<Article>
{
	private CommentRepository commentRepository;
	private ProfileRepository profileRepository;

	public Profile getAuthor( Article article ) {
		return profileRepository.findById( article.getAuthorId() ).orElse( null );
	}

	public List<Comment> getComments( Article article ) {
		return commentRepository.findByArticleId( article.getId() );
	}
}