package ax.application.repositories;

import ax.application.business.Customer;
import com.foreach.across.core.annotations.Exposed;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

@Exposed
public interface CustomerRepository extends MongoRepository<Customer, String>
{
	Customer findByFirstName( String firstName );

	List<Customer> findByLastName( String lastName );
}