package ax;

import ax.client.SpringCloudConfigClientApplication;
import ax.client.application.BoundComponent;
import ax.client.application.CustomSettings;
import ax.server.SpringCloudConfigServerApplication;
import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import com.foreach.across.modules.web.AcrossWebModule;
import com.foreach.across.modules.web.config.AcrossWebModuleDevSettings;
import org.junit.jupiter.api.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
public class TestSpringCloudConfig
{
	private static ServletWebServerApplicationContext serverContext, clientContext;

	private static RestTemplate server;
	private static RestTemplate client;

	private BoundComponent rootComponent;
	private BoundComponent moduleComponent;
	private CustomSettings moduleSettings;
	private Environment moduleEnvironment;
	private Environment acrossWebModuleEnvironment;
	private AcrossWebModuleDevSettings acrossWebModuleDevSettings;

	@BeforeAll
	public static void start() {
		serverContext = SpringCloudConfigServerApplication.runApplication( "--server.port=0" );
		int serverPort = serverContext.getWebServer().getPort();
		server = new RestTemplateBuilder().rootUri( "http://localhost:" + serverPort ).build();

		clientContext = SpringCloudConfigClientApplication.runApplication( "--server.port=0", "--spring.cloud.config.uri=http://localhost:" + serverPort );
		int clientPort = clientContext.getWebServer().getPort();
		client = new RestTemplateBuilder().rootUri( "http://localhost:" + clientPort ).build();
	}

	@AfterAll
	public static void stop() {
		clientContext.close();
		serverContext.close();
	}

	@BeforeEach
	public void fetchComponents() {
		AcrossContextBeanRegistry beanRegistry = clientContext.getBean( AcrossContextBeanRegistry.class );
		rootComponent = clientContext.getBean( "rootComponent", BoundComponent.class );
		String clientModuleName = SpringCloudConfigClientApplication.class.getSimpleName() + "Module";
		moduleComponent = beanRegistry.getBeanOfTypeFromModule( clientModuleName, BoundComponent.class );
		moduleSettings = beanRegistry.getBeanOfType( CustomSettings.class );
		moduleEnvironment = beanRegistry.getBeanOfTypeFromModule( clientModuleName, Environment.class );

		acrossWebModuleDevSettings = beanRegistry.getBeanOfTypeFromModule( AcrossWebModule.NAME, AcrossWebModuleDevSettings.class );
		acrossWebModuleEnvironment = beanRegistry.getBeanOfTypeFromModule( AcrossWebModule.NAME, Environment.class );
		assertNotSame( moduleEnvironment, acrossWebModuleEnvironment );
	}

	@Test
	@Disabled("FIXME: https://github.com/spring-cloud/spring-cloud-config/issues/1777 ?")
	public void propertyChangesAreAvailableImmediatelyOnTheServer() {
		String expected = UUID.randomUUID().toString();
		updateRandomValue( expected );
		enableRenderViewElementNames( false );

		String json = server.getForObject( "/myclient/development", String.class );
		assertTrue( json.contains( "\"custom.property\":\"" + expected + "\"" ) );
		assertTrue( json.contains( "\"across.web.development.renderViewElementNames\":\"false\"" ) );
	}

	@Test
	@Disabled("FIXME: https://github.com/spring-cloud/spring-cloud-config/issues/1777 ?")
	public void refreshPropertiesOnClient() {
		String initialValue = UUID.randomUUID().toString();
		updateRandomValue( initialValue );
		enableRenderViewElementNames( true );
		client.postForObject( "/actuator/refresh", Collections.emptyMap(), String.class );

		expectRootValues( initialValue );
		expectModuleValues( initialValue, true );

		String randomValue = UUID.randomUUID().toString();
		updateRandomValue( randomValue );
		enableRenderViewElementNames( false );
		String response = client.postForObject( "/actuator/refresh", Collections.emptyMap(), String.class );
		assertTrue( response.contains( "custom.property" ) );
		assertTrue( response.contains( "across.web.development.renderViewElementNames" ) );

		expectRootValues( randomValue );
		expectModuleValues( randomValue, false );
	}

	private void expectRootValues( String customProperty ) {
		assertEquals( customProperty, clientContext.getEnvironment().getProperty( "custom.property" ) );
		assertEquals( "This is a fixed message.", rootComponent.getFixedMessage() );
		assertEquals( customProperty, rootComponent.getCustomProperty() );
		assertEquals( customProperty, client.getForObject( "/application/customProperty", String.class ) );
	}

	private void expectModuleValues( String customProperty, boolean renderViewElementNames ) {
		assertEquals( customProperty, moduleEnvironment.getProperty( "custom.property" ) );
		assertEquals( customProperty, acrossWebModuleEnvironment.getProperty( "custom.property" ) );

		assertEquals( "This is a fixed message.", moduleComponent.getFixedMessage() );
		assertEquals( customProperty, moduleComponent.getCustomProperty() );
		assertEquals( customProperty, moduleSettings.getProperty() );

		assertEquals( renderViewElementNames, acrossWebModuleDevSettings.isRenderViewElementNames() );

		assertEquals( customProperty, client.getForObject( "/applicationModule/customProperty", String.class ) );
	}

	private void updateRandomValue( String value ) {
		assertEquals( "OK", server.getForObject( "/customProperty?value=" + value, String.class ) );
	}

	private void enableRenderViewElementNames( boolean enabled ) {
		assertEquals( "OK", server.getForObject( "/renderViewElementNames?enabled=" + enabled, String.class ) );
	}
}
