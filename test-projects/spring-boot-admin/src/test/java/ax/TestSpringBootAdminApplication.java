package ax;

import com.foreach.across.core.context.info.AcrossContextInfo;
import com.foreach.across.modules.web.AcrossWebModule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore //TODO:FIXME
public class TestSpringBootAdminApplication
{
	@Autowired
	private AcrossContextInfo contextInfo;

	@Test
	public void shouldBootStrap() {
		assertTrue( contextInfo.hasModule( "SpringBootAdminApplicationModule" ) );
		assertTrue( contextInfo.hasModule( AcrossWebModule.NAME ) );
	}
}