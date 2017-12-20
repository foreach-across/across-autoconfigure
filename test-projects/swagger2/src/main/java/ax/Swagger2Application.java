package ax;

import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Steven Gentens
 */
@AcrossApplication(
		modules = { AcrossWebModule.NAME }
)
@EnableSwagger2
public class Swagger2Application
{
	public static void main( String[] args ) {
		SpringApplication.run( Swagger2Application.class );
	}

	@Bean
	public Docket api() {
		return new Docket( DocumentationType.SWAGGER_2 )
				.select()
				.apis( RequestHandlerSelectors.any() )
				.paths( PathSelectors.any() )
				.build()
				.apiInfo( apiInfo() );
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title( "Spring REST Sample with Swagger" )
				.description( "Spring REST Sample with Swagger" )
				.termsOfServiceUrl( "http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open" )
				.contact( new Contact( "John Doe", "", "john.doe@example.com" ) )
				.license( "Apache License Version 2.0" )
				.licenseUrl( "https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE" )
				.version( "2.0" )
				.build();
	}
}
