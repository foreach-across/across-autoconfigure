package ax;

import ax.application.business.Person;
import ax.application.repositories.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.naming.ldap.LdapName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSpringBootDataLdap
{
	@Autowired
	private PersonRepository personRepository;

	@Test
	public void shouldBootstrapAndFindPerson() throws Exception {
		assertEquals( 7, personRepository.count() );

		Person person = personRepository.findByUid( "joe" );
		assertNotNull( person );
		assertEquals( "joe", person.getUid() );
		assertEquals( "Joe Smeth", person.getCn() );
		assertEquals( "Smeth", person.getSn() );

		assertEquals( person, personRepository.findById( new LdapName( "uid=joe,ou=otherpeople,dc=springframework,dc=org" ) ).orElse( null ) );
	}
}
