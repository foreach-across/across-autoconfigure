package ax.application.repositories;

import ax.application.business.User;
import com.couchbase.client.java.query.QueryScanConsistency;
import com.foreach.across.core.annotations.Exposed;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.ScanConsistency;

import java.util.Optional;

@Exposed
public interface UserRepository extends CouchbaseRepository<User, String>
{
	@Override
	@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
	long count();

	@Override
	@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
	Optional<User> findById(String s);
}
