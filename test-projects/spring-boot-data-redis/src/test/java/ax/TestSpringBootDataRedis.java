package ax;

import ax.business.Person;
import ax.repositories.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootDataRedis.class)
public class TestSpringBootDataRedis {

    @Autowired
    private PersonRepository repository;

    @Test
    public void shouldBootstrap() throws IOException {
        assertNotNull(repository);

        repository.deleteAll();

        repository.save(new Person("Alice", "Springs"));
        repository.save(new Person("Charlotte", "Sits"));

        assertEquals(2, repository.count());

        // The following will return no results as the firstname field is not indexed in Redis
        assertEquals(0, repository.findByFirstname("Alice").size());
    }
}
