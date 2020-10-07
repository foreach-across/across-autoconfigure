package ax;

import ax.application.business.Product;
import ax.application.repositories.ProductRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestSpringBootDataSolrApplication extends AbstractIntegrationTest
{
	@Autowired
	private ProductRepository repository;

	@Test
	public void applicationAndSolrBootstraps() {
		assertNotNull( repository );

		assertEquals(0, repository.count());
		repository.deleteAll();

		repository.save( new Product( "TWINX2048-3200PRO", "CORSAIR  XMS 2GB (2 x 1GB) 184-Pin DDR SDRAM Unbuffered DDR 400 (PC 3200) Dual Channel Kit System Memory - Retail",
				true,  "Corsair Microsystems Inc.", "corsair", Date.from(Instant.now()), 185, 5 ) );

		assertEquals( 1, repository.count() );
		assertEquals( 1, repository.countByManufacturerId("corsair") );

		Optional<Product> holder = repository.findById( "TWINX2048-3200PRO" );
		assertTrue( holder.isPresent() );
		Product product = holder.get();

		assertEquals( "TWINX2048-3200PRO", product.getId() );
		assertEquals( "CORSAIR  XMS 2GB (2 x 1GB) 184-Pin DDR SDRAM Unbuffered DDR 400 (PC 3200) Dual Channel Kit System Memory - Retail", product.getName() );
		assertTrue( product.isInStock() );
		assertEquals( "Corsair Microsystems Inc.", product.getManu() );
		assertEquals( 185, product.getPrice(), 0 );
		assertEquals( 5, product.getPopularity() );
		assertEquals( "corsair", product.getManufacturerId() );


	}
}
