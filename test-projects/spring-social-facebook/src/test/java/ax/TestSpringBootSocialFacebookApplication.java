package ax;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.social.FacebookProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author Steven Gentens
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestSpringBootSocialFacebookApplication
{
	@Autowired
	private FacebookProperties facebookProperties;

	@Test
	public void shouldBootstrap() {
		FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory( facebookProperties.getAppId(), facebookProperties.getAppSecret() );
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add( "redirect_uri", "" );
		params.add( "grant_type", "client_credentials" );
		AccessGrant accessGrant = facebookConnectionFactory.getOAuthOperations().exchangeCredentialsForAccess( "testaxfacebook@outlook.com", "t3st1598",
		                                                                                                       params );
		String token = accessGrant.getAccessToken();
		assertNotNull( token );

		Facebook facebook = new FacebookTemplate( token );
		PagedList<Page> foreachPages = facebook.pageOperations().search( "foreach" );
		assertNotNull( foreachPages );
		assertFalse( foreachPages.isEmpty() );
	}

}
