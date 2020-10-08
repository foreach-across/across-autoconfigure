package ax;

//import io.github.bonigarcia.wdm.PhantomJsDriverManager;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ContainerNetwork;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Steven Gentens
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Swagger2Application.class)
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

	@Test
	@SneakyThrows
	public void shouldContainModel() {
		BrowserWebDriverContainer<?> container = new BrowserWebDriverContainer<>()
				.withCapabilities(new ChromeOptions());

		boolean insideDocker = Files.exists( Paths.get( "/.dockerenv" ));
		String url;

		if( !insideDocker ) {
			url = "http://host.testcontainers.internal:" + port + SWAGGER_UI_ENDPOINT;
			Testcontainers.exposeHostPorts( port );
			container.start();
		} else {
			DockerClient dockerClient = container.getDockerClient();
			String hostname = InetAddress.getLocalHost().getHostName();
			InspectContainerResponse maven = dockerClient.inspectContainerCmd( hostname ).exec();
			ContainerNetwork bridge = maven.getNetworkSettings().getNetworks().get( "bridge" );
			String ip = bridge.getIpAddress();

			container.start();

			dockerClient.connectToNetworkCmd().withContainerId(container.getContainerId()).withNetworkId(bridge.getNetworkID()).exec();

			System.out.println("======================");
			System.out.println(hostname + " -> " + ip + " /  container connecting to " + bridge.getNetworkID() );
			System.out.println("======================");

			url = "http://" + ip + ":" + port + SWAGGER_UI_ENDPOINT;
		}

		System.out.println("starting request to: " + url );
		System.out.println("started");
		WebDriver driver = container.getWebDriver();
		driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
		System.out.println("driver" + driver);
		driver.navigate().to(  url );

		Wait<WebDriver> wait = new FluentWait<>( driver )
				.withTimeout( Duration.ofSeconds( 30 ) )
				.pollingEvery( Duration.ofSeconds( 3 ) )
				.ignoring( NoSuchElementException.class );

		WebElement apiController = wait.until( ExpectedConditions.visibilityOfElementLocated( By.partialLinkText( "api-controller" ) ) );
		apiController.click();

		//WebElement apiItemPath = driver.findElement( By.partialLinkText( "/api/item/{id}" ) );
		WebElement apiItemPath = wait.until( ExpectedConditions.visibilityOfElementLocated( By.partialLinkText( "/api/item/{id}" ) ) );
		apiItemPath.click();

		/*
		WebElement exampleValueForPath = driver.findElement( By.className( "snippet_json" ) );
		//( new WebDriverWait( driver, 10 ) ).until( ExpectedConditions.visibilityOfElementLocated( By.className( "snippet_json" ) ) );

		assertNotNull( exampleValueForPath );
		assertEquals( EXAMPLE_VALUE_SNIPPET, exampleValueForPath.getText() );
		 */
		container.stop();
	}
}
