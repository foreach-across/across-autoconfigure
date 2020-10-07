package ax.application.business;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Table
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@Setter
public class Customer
{
	@PrimaryKey
	private UUID id;
	private String firstName;
	private String lastName;
}
