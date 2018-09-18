package ax.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;

import java.io.IOException;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@Component
class ElasticSearchConfig
{
	@Bean(destroyMethod = "stop")
	public EmbeddedElastic elasticSearchTestNode() throws IOException, InterruptedException {
		return EmbeddedElastic.builder()
		                      .withElasticVersion( "5.6.10" )
		                      .build().start();
	}
}
