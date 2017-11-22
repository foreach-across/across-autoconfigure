package ax.repositories;

import ax.business.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {
    long countByManufacturerId(String id);
}

