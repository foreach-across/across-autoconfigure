package ax;

import io.github.bonigarcia.wdm.PhantomJsDriverManager;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Steven Gentens
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ITSwagger2Application
{
	@org.springframework.beans.factory.annotation.Value("${local.server.port}")
	private int port;

	private static final String SWAGGER_UI_ENDPOINT = "/swagger-ui.html";
	private static final String EXAMPLE_VALUE_SNIPPET = "{\n" +
			"  \"amount\": 0,\n" +
			"  \"created\": \"2017-12-19T13:39:14.007Z\",\n" +
			"  \"deleted\": true,\n" +
			"  \"id\": 0,\n" +
			"  \"name\": \"string\",\n" +
			"  \"relatedItems\": [\n" +
			"    {}\n" +
			"  ]\n" +
			"}";
	private static String PHANTOMJS_BINARY;
	private String URL;

	@BeforeClass
	public static void setupClass() {
		PhantomJsDriverManager.getInstance().setup();
	}

	@Before
	public void setUp() {
		URL = "http://127.0.0.1:" + port + SWAGGER_UI_ENDPOINT;
		PHANTOMJS_BINARY = System.getProperty( "phantomjs.binary.path" );
	}

	@Test
	public void shouldContainModel() {
		final DesiredCapabilities capabilities = new DesiredCapabilities();
		// Configure our WebDriver to support JavaScript and be able to find the PhantomJS binary
		capabilities.setJavascriptEnabled( true );
		capabilities.setCapability( "takesScreenshot", false );
		capabilities.setCapability(
				PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				PHANTOMJS_BINARY
		);
		WebDriver driver = new PhantomJSDriver( capabilities );
		driver.navigate().to( URL );

		WebElement apiController = driver.findElement( By.partialLinkText( "api-controller" ) );
		apiController.click();

		WebElement apiItemPath = driver.findElement( By.partialLinkText( "/api/item/{id}" ) );
		( new WebDriverWait( driver, 10 ) ).until( ExpectedConditions.visibilityOfElementLocated( By.partialLinkText( "/api/item/{id}" ) ) );
		apiItemPath.click();

		WebElement exampleValueForPath = driver.findElement( By.className( "snippet_json" ) );
		( new WebDriverWait( driver, 10 ) ).until( ExpectedConditions.visibilityOfElementLocated( By.className( "snippet_json" ) ) );

		assertNotNull( exampleValueForPath );
		assertEquals( EXAMPLE_VALUE_SNIPPET, exampleValueForPath.getText() );
		driver.quit();
	}
}
