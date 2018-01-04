package ax.application.business;

import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Data
@Entry(objectClasses = { "person", "top" })
public class Person
{
	@Id
	private Name dn;

	@Attribute(name = "uid")
	private String uid;

	@Attribute(name = "cn")
	private String cn;

	@Attribute(name = "sn")
	private String sn;

	@Attribute(name = "userPassword")
	private String userPassword;
}
