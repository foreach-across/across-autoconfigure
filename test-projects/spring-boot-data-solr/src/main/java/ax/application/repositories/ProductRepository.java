package ax.application.repositories;

import ax.application.business.Product;
import com.foreach.across.core.annotations.Exposed;
import org.springframework.data.repository.CrudRepository;

@Exposed
public interface ProductRepository extends CrudRepository<Product, String>
{
	long countByManufacturerId( String id );
}

