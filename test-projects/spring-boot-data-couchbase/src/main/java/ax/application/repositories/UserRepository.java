package ax.application.repositories;

import ax.application.business.User;
import com.foreach.across.core.annotations.Exposed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

@Exposed
@ViewIndexed(designDoc = "user", viewName = "all")
public interface UserRepository extends CouchbaseRepository<User, String>
{

}
