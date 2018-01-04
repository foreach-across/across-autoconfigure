package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
public class SpringBootDataElasticSearch
{
	static {
		try {
			FileUtils.deleteDirectory( new File( System.getProperty( "user.dir" ) + "/data" ) );
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	@Autowired
	private Client client;

	public static void main( String[] args ) {
		SpringApplication.run( SpringBootDataElasticSearch.class );
	}

	@PostConstruct
	public void createIndex() {
		Settings indexSettings = Settings.settingsBuilder()
		                                 .put( "number_of_shards", 5 )
		                                 .put( "number_of_replicas", 1 )
		                                 .build();
		CreateIndexRequest indexRequest = new CreateIndexRequest( "personidx", indexSettings );
		client.admin().indices().create( indexRequest ).actionGet();
	}
}
