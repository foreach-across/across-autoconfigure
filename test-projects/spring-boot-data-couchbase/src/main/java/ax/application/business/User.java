package ax.application.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Document
@EqualsAndHashCode(of="id")
@Getter
@Setter
public class User
{
	@Id
	private String id;

	@Field
	private String firstName;

	@Field
	private String lastName;
}
