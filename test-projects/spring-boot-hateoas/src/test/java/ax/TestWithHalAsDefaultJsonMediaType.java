package ax;

import ax.application.business.Company;
import ax.application.business.Employee;
import ax.modules.custom.book.Book;
import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import com.foreach.across.test.support.config.MockMvcConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.core.ControllerEntityLinks;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Gunther Van Geetsom
 * @since 1.0.2
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = { SpringBootHateoasApplication.class, MockMvcConfiguration.class },
		properties = { "spring.hateoas.use-hal-as-default-json-media-type=true" })
public class TestWithHalAsDefaultJsonMediaType
{
	@Autowired
	private AcrossContextBeanRegistry beanRegistry;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void controllerFromApplicationModuleIsHAL() throws Exception {
		mockMvc.perform( get( "/company/1" ) )
		       .andExpect( status().isOk() )
		       .andExpect( jsonPath( "_links.employees.href", equalTo( "http://localhost/employees" ) ) )
		       .andExpect( jsonPath( "_links.employee[1].href", equalTo( "http://localhost/employees/Jozef" ) ) )
		       .andExpect( jsonPath( "_links.book.href", equalTo( "http://localhost/book/someBook" ) ) );
	}

	@Test
	public void controllerFromSharedModuleIsHAL() throws Exception {
		mockMvc.perform( get( "/book/someBook" ) )
		       .andExpect( status().isOk() )
		       .andExpect( jsonPath( "_links.self.href", equalTo( "http://localhost/book/someBook" ) ) )
		       .andExpect( jsonPath( "_links.book.href", equalTo( "http://localhost/book/anotherBook" ) ) );
	}

	@Test
	public void greetingResponseIsAlwaysHAL() throws Exception {
		mockMvc.perform( get( "/greeting" ).accept( MediaTypes.HAL_JSON ) )
		       .andExpect( status().isOk() )
		       .andExpect( jsonPath( "_links.self.href", equalTo( "http://localhost/greeting?name=World" ) ) );
		mockMvc.perform( get( "/greeting" ).accept( MediaType.APPLICATION_JSON ) )
		       .andExpect( status().isOk() )
		       .andExpect( jsonPath( "_links.self.href", equalTo( "http://localhost/greeting?name=World" ) ) );
	}

	@Test
	public void entityLinksShouldFindControllers() {
		assertTrue( beanRegistry.getBeanOfType( ControllerEntityLinks.class ).supports( Company.class ) );
		assertTrue( beanRegistry.getBeanOfType( ControllerEntityLinks.class ).supports( Employee.class ) );
		assertTrue( beanRegistry.getBeanOfType( ControllerEntityLinks.class ).supports( Book.class ) );
	}
}
