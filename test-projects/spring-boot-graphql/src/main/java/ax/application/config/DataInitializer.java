package ax.application.config;

import ax.application.domain.article.Article;
import ax.application.domain.article.ArticleRepository;
import ax.application.domain.comment.Comment;
import ax.application.domain.comment.CommentRepository;
import ax.application.domain.profile.Profile;
import ax.application.domain.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class DataInitializer
{
	private final CommentRepository commentRepository;
	private final ArticleRepository articleRepository;
	private final ProfileRepository profileRepository;

	@PostConstruct
	public void initializeData() {
		Profile author = profileRepository.save( new Profile( null, "g00glen00b", "The author of this blog" ) );
		Profile admin = profileRepository.save( new Profile( null, "admin", "The administrator of this blog" ) );
		Article article1 = articleRepository.save( new Article( null, "Hello wold", "This is a hello world", author.getId() ) );
		Article article2 = articleRepository.save( new Article( null, "Foo", "Bar", admin.getId() ) );
		commentRepository.save( new Comment( null, "Do you like this article?", article1.getId(), author.getId() ) );
		commentRepository.save( new Comment( null, "This is a great article", article1.getId(), admin.getId() ) );
		commentRepository.save( new Comment( null, "This is a comment", article2.getId(), admin.getId() ) );
	}
}
