package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.boot.SpringApplication;

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
	/***
	 * Required because elastic search does not clean up {@link org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration.DEFAULTS}
	 ***/
	static {
		try {
			FileUtils.deleteDirectory( new File( System.getProperty( "user.dir" ) + "/data" ) );
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public static void main( String[] args ) {
		SpringApplication.run( SpringBootDataElasticSearch.class );
	}
}
