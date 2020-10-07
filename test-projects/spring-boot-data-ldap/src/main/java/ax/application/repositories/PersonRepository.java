package ax.application.repositories;

import ax.application.business.Person;
import org.springframework.data.repository.CrudRepository;

import javax.naming.Name;

public interface PersonRepository extends CrudRepository<Person, Name>
{
	Person findByUid( String uid );
}
