package ax;

import ax.application.business.Company;
import ax.application.business.Employee;
import com.foreach.across.core.context.registry.AcrossContextBeanRegistry;
import com.foreach.across.test.support.config.MockMvcConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.core.ControllerEntityLinks;
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
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = { SpringBootHateoasApplication.class, MockMvcConfiguration.class })
public class TestSpringBootHateoasWebApplication
{

	@Autowired
	protected AcrossContextBeanRegistry beanRegistry;
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void entityLinksShouldWork() throws Exception {
		mockMvc.perform( get( "/company/1" ) )
		       .andExpect( status().isOk() )
		       .andExpect( jsonPath( "links[1].rel", equalTo( "employees" ) ) );
	}

	@Test
	public void entityLinksShouldFoundControllers() {
		assertTrue( beanRegistry.getBeanOfType( ControllerEntityLinks.class ).supports( Company.class ) );
		assertTrue( beanRegistry.getBeanOfType( ControllerEntityLinks.class ).supports( Employee.class ) );
	}
}
