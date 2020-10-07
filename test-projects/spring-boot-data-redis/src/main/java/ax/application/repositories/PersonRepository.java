package ax.application.repositories;

import ax.application.business.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, String>
{
	List<Person> findByFirstname( String firstname );
}
