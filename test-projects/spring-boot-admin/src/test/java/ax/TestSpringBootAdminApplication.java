package ax;

import com.foreach.across.core.context.info.AcrossContextInfo;
import com.foreach.across.modules.web.AcrossWebModule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled //TODO:FIXME
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
