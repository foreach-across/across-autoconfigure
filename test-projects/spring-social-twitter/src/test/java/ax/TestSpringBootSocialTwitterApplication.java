package ax;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.social.TwitterProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author Steven Gentens
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSpringBootSocialTwitterApplication
{
	@Autowired
	private TwitterProperties twitterProperties;

	@Test
	public void shouldBootstrap() {
		Twitter twitter = new TwitterTemplate( twitterProperties.getAppId(), twitterProperties.getAppSecret() );
		TwitterProfile foreachTwitter = twitter.userOperations().getUserProfile( "@beforeach" );
		assertNotNull( foreachTwitter );
		assertEquals( "foreach", foreachTwitter.getName() );

		SearchResults searchResults = twitter.searchOperations().search( "@beforeach" );
		assertFalse( searchResults.getTweets().isEmpty() );
	}

}
