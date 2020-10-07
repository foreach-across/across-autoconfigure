package ax.application.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "personidx", type = "person")
public class Person
{
	@Id
	private String id;

	private String firstname;
	private String lastname;

	public Person( String firstname, String lastname ) {
		this.firstname = firstname;
		this.lastname = lastname;
	}
}
