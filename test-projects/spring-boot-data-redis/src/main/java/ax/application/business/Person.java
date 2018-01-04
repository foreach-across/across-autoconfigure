package ax.application.business;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("persons")
@Data
public class Person
{
	@Id
	String id;
	String firstname;
	String lastname;

	public Person( String firstname, String lastname ) {
		this.firstname = firstname;
		this.lastname = lastname;
	}
}

