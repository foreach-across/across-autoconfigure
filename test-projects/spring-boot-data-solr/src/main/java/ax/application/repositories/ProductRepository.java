package ax.application.repositories;

import ax.application.business.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {
    long countByManufacturerId(String id);
}

