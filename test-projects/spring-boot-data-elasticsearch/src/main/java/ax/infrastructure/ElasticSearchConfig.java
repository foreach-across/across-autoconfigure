package ax.infrastructure;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
class ElasticSearchConfig
{
	private final Client client;

	static {
		try {
			FileUtils.deleteDirectory( new File( System.getProperty( "user.dir" ) + "/data" ) );
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
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
