package ax.infrastructure;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
class ElasticSearchConfig
{
	private final Client client;

	@PostConstruct
	public void createIndex() {
		Settings indexSettings = Settings.builder()
		                                 .put( "number_of_shards", 5 )
		                                 .put( "number_of_replicas", 1 )
		                                 .build();
		CreateIndexRequest indexRequest = new CreateIndexRequest( "personidx", indexSettings );
		client.admin().indices().create( indexRequest ).actionGet();
	}
}
