package ax.application.repositories;

import ax.application.business.Person;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface PersonRepository extends GraphRepository<Person>
{
	Person findByName( String name );
}