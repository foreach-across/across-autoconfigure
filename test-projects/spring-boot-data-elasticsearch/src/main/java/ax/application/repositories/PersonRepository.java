package ax.application.repositories;

import ax.application.business.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PersonRepository extends ElasticsearchRepository<Person, String> {
    List<Person> findByFirstname(String firstname);
}
