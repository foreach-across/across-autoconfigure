package ax;

//import io.github.bonigarcia.wdm.PhantomJsDriverManager;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ContainerNetwork;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.output.BaseConsumer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.dockerclient.DockerClientConfigUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
			"    null\n" +
			"  ]\n" +
			"}";

	@Test
	@SneakyThrows
	public void shouldContainModel() {
		BrowserWebDriverContainer<?> container = new BrowserWebDriverContainer<>()
				.withCapabilities(new ChromeOptions());
		container.withEnv( "GRID_DEBUG", "true" );

		
		String url;

		if( !DockerClientConfigUtils.IN_A_CONTAINER) {
			url = "http://host.testcontainers.internal:" + port + SWAGGER_UI_ENDPOINT;
			Testcontainers.exposeHostPorts( port );
			container.start();
		} else {
			DockerClient dockerClient = container.getDockerClient();
			String hostname = InetAddress.getLocalHost().getHostName();
			InspectContainerResponse maven = dockerClient.inspectContainerCmd( hostname ).exec();
			ContainerNetwork network = maven.getNetworkSettings().getNetworks().values()
			                                .stream()
			                                .findFirst()
			                                .orElseThrow( () -> new RuntimeException( "Cannot find a network" ) );
			String ip = network.getIpAddress();

			container.start();

			dockerClient.connectToNetworkCmd().withContainerId(container.getContainerId()).withNetworkId(network.getNetworkID()).exec();

			System.out.println("======================");
			System.out.println(hostname + " -> " + ip + " /  container connecting to " + network.getNetworkID() );
			System.out.println("======================");

			url = "http://" + ip + ":" + port + SWAGGER_UI_ENDPOINT;
		}

		StdOutConsumer toStringConsumer = new StdOutConsumer();
		container.followOutput( toStringConsumer, OutputFrame.OutputType.STDOUT);

		System.out.println("Starting requests to: " + url );
		// Follow the documentation: https://java.testcontainers.org/modules/webdriver_containers/
		RemoteWebDriver driver = new RemoteWebDriver(container.getSeleniumAddress(), new ChromeOptions());
		System.out.println("Driver: " + driver);
		driver.setLogLevel( Level.INFO );

		try {
			driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
		} catch ( UnreachableBrowserException e ) {
			e.printStackTrace();
			Thread.sleep( 10000 );
		}

		driver.navigate().to(  url );

		Wait<RemoteWebDriver> wait = new FluentWait<>( driver )
				.withTimeout( Duration.ofSeconds( 30 ) )
				.pollingEvery( Duration.ofSeconds( 3 ) )
				.ignoring( NoSuchElementException.class );

		WebElement apiController = wait.until( ExpectedConditions.visibilityOfElementLocated( By.partialLinkText( "api-controller" ) ) );
		apiController.click();

		//WebElement apiItemPath = driver.findElement( By.partialLinkText( "/api/item/{id}" ) );
		WebElement apiItemPath = wait.until( ExpectedConditions.visibilityOfElementLocated( By.partialLinkText( "/api/item/{id}" ) ) );
		apiItemPath.click();

		WebElement exampleValueForPath= wait.until( ExpectedConditions.visibilityOfElementLocated( By.className( "highlight-code" ) ) );

		assertNotNull( exampleValueForPath );
		assertEquals( EXAMPLE_VALUE_SNIPPET, exampleValueForPath.getText() );
		container.stop();
	}

	private static class StdOutConsumer extends BaseConsumer<StdOutConsumer> {
		@Override
		public void accept( OutputFrame outputFrame ) {
			if (outputFrame.getBytes() != null) {
				System.out.print( "BrowserWebDriverContainer ===> " + outputFrame.getUtf8String() );
			}
		}
	}
}
