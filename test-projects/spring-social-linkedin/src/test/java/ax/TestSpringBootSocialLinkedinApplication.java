package ax;

import io.github.bonigarcia.wdm.PhantomJsDriverManager;
import liquibase.util.StringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.social.LinkedInProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.social.linkedin.api.Company;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * @author Steven Gentens
 */
@Ignore("Changes as LinkedIn side? - social support to be removed in future Spring Boot version anyway")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestSpringBootSocialLinkedinApplication
{
	@Autowired
	private LinkedInProperties linkedInProperties;

	private WebDriver driver;

	@BeforeClass
	public static void setupClass() {
		PhantomJsDriverManager.getInstance().setup();
	}

	@Before
	public void setUp() {
		final DesiredCapabilities capabilities = new DesiredCapabilities();
		// Configure our WebDriver to support JavaScript and be able to find the PhantomJS binary
		capabilities.setJavascriptEnabled( true );
		capabilities.setCapability( "takesScreenshot", false );
		capabilities.setCapability(
				PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				System.getProperty( "phantomjs.binary.path" )
		);
		driver = new PhantomJSDriver( capabilities );
	}

	@Test
	public void shouldBootstrap() {
		LinkedInConnectionFactory linkedInConnectionFactory = new LinkedInConnectionFactory( linkedInProperties.getAppId(), linkedInProperties.getAppSecret() );
		UriComponents authorization = UriComponentsBuilder.fromUriString( "https://www.linkedin.com/uas/oauth2/authorization" )
		                                                  .queryParams( getAuthorizationParameters( linkedInConnectionFactory ) )
		                                                  .build();

		driver.navigate().to( authorization.toUriString() );
		( new WebDriverWait( driver, 10 ) ).until( ExpectedConditions.visibilityOfElementLocated( By.partialLinkText( "Join LinkedIn" ) ) );

		WebElement email = driver.findElement( By.id( "session_key-oauth2SAuthorizeForm" ) );
		email.sendKeys( "testaxfacebook@outlook.com" );
		WebElement password = driver.findElement( By.id( "session_password-oauth2SAuthorizeForm" ) );
		password.sendKeys( "t3st1598" );

		WebElement submit = driver.findElement( By.name( "authorize" ) );
		submit.click();
		( new WebDriverWait( driver, 10 ) ).until( ExpectedConditions.visibilityOfElementLocated( By.id( "hello-world" ) ) );
		String url = driver.getCurrentUrl();

		String code = getCode( url );
		assertTrue( StringUtils.isNotEmpty( code ) );

		UriComponents authentication = UriComponentsBuilder.fromUriString( "https://www.linkedin.com/uas/oauth2/accessToken" )
		                                                   .queryParams( getAuthenticationParameters( code ) )
		                                                   .build();
		RestTemplate restTemplate = new RestTemplate();
		Map response = restTemplate.getForObject( authentication.toUriString(), Map.class );
		String token = (String) response.get( "access_token" );
		assertNotNull( token );

		LinkedIn linkedin = new LinkedInTemplate( token );
		List<Company> companies = linkedin.companyOperations().search( "Foreach" ).getCompanies();
		assertFalse( companies.isEmpty() );
		Company foreach = companies.stream().filter( c -> c.getName().equalsIgnoreCase( "foreach" ) ).findFirst().get();
		assertNotNull( foreach );
		assertEquals( "https://www.foreach.be/", foreach.getWebsiteUrl() );
		driver.quit();
	}

	private String getCode( String url ) {
		Pattern pattern = Pattern.compile( "code=(.*)&" );
		Matcher matcher = pattern.matcher( url );
		assertTrue( matcher.find() );
		return matcher.group();
	}

	private MultiValueMap<String, String> getAuthenticationParameters( String code ) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.set( "redirect_uri", "http://localhost:8080" );
		params.set( "code", code.substring( 5, code.length() - 1 ) );
		params.set( "grant_type", "authorization_code" );
		params.set( "client_id", linkedInProperties.getAppId() );
		params.set( "client_secret", linkedInProperties.getAppSecret() );
		return params;
	}

	private MultiValueMap<String, String> getAuthorizationParameters( LinkedInConnectionFactory linkedInConnectionFactory ) {
		MultiValueMap<String, String> variables = new LinkedMultiValueMap<>();
		variables.add( "client_id", linkedInProperties.getAppId() );
		variables.add( "response_type", "code" );
		variables.add( "redirect_uri", "http%3A%2F%2Flocalhost%3A8080" );
		variables.add( "state", linkedInConnectionFactory.generateState() );
		return variables;
	}

}
