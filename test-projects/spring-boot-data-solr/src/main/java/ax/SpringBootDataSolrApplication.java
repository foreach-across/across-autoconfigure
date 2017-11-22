package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.apache.solr.client.solrj.embedded.JettySolrRunner;
import org.springframework.boot.SpringApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@AcrossApplication(
        modules = AcrossWebModule.NAME
)
public class SpringBootDataSolrApplication {
    private JettySolrRunner jetty;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataSolrApplication.class);
    }

    @PostConstruct
    public void setup() throws Exception {
        jetty = new JettySolrRunner("./src/solr", "/solr", 8983);
        jetty.start();
    }

    @PreDestroy
    public void destroy() throws Exception {
        if (jetty != null) {
            jetty.stop();
        }
    }
}
