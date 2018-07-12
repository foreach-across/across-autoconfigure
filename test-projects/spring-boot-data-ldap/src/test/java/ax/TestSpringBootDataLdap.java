package ax;

import ax.application.business.Person;
import ax.application.repositories.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.ldap.LdapName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSpringBootDataLdap
{

	@Autowired
	private PersonRepository personRepository;

	@Test
	public void shouldBootstrapAndReturnWhitelabelErrorPage() throws Exception {
		assertEquals( 7, personRepository.count() );

		Person person = personRepository.findByUid( "joe" );
		assertNotNull( person );
		assertEquals( "joe", person.getUid() );
		assertEquals( "Joe Smeth", person.getCn() );
		assertEquals( "Smeth", person.getSn() );

		assertEquals( person, personRepository.findById( new LdapName( "uid=joe,ou=otherpeople,dc=springframework,dc=org" ) ).orElse( null ) );
	}
}
