package ax.application.business;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("persons")
@Data
public class Person
{
	@Id
	String id;
	String firstname;

	@Indexed
	String lastname;

	public Person( String firstname, String lastname ) {
		this.firstname = firstname;
		this.lastname = lastname;
	}
}

