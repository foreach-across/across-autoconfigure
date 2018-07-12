package ax.application.business;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Table
public class Customer
{

	@PrimaryKey
	private UUID id;

	private String firstName;

	private String lastName;

	public Customer() {
	}

	public Customer( UUID id, String firstName, String lastName ) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		Customer customer = (Customer) o;
		return Objects.equals( id, customer.id );
	}

	@Override
	public int hashCode() {
		return Objects.hash( id );
	}
}
