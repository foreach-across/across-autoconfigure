package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.apache.commons.io.FileUtils;
import org.neo4j.helpers.collection.Pair;
import org.neo4j.server.CommunityBootstrapper;
import org.neo4j.server.NeoServer;
import org.neo4j.server.ServerBootstrapper;
import org.springframework.boot.SpringApplication;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Marc Vanbrabant
 * @since 1.0.0
 */
@AcrossApplication(
		modules = { AcrossWebModule.NAME }
)
public class SpringBootDataNeo4jApplication
{
	private static final ServerBootstrapper serverBootstrapper;
	public static final File neoDbTempDir = new File( "./neoDbTempDir" );

	static {
		// Don't ask ...
		serverBootstrapper = new CommunityBootstrapper();
		serverBootstrapper.start(
				neoDbTempDir,
				Optional.empty(), // omit configfile, properties follow
				Pair.of( "dbms.connector.http.address", "127.0.0.1:7474" ),
				Pair.of( "dbms.connector.http.enabled", "true" ),
				Pair.of( "dbms.connector.bolt.enabled", "false" ),
				Pair.of( "dbms.security.auth_enabled", "false" ),

				// allow the shell connections via port 1337 (default)
				Pair.of( "dbms.shell.enabled", "true" ),
				Pair.of( "dbms.shell.host", "127.0.0.1" ),
				Pair.of( "dbms.shell.port", "1337" )
		);
		NeoServer neoServer = serverBootstrapper.getServer();
		neoServer.getDatabase().getGraph();
	}

	public static void main( String[] args ) throws InterruptedException {
		SpringApplication.run( SpringBootDataNeo4jApplication.class );
	}

	@PreDestroy
	public void destroy() throws IOException {
		serverBootstrapper.stop();
		FileUtils.deleteDirectory( neoDbTempDir );
	}
}
