package ax;

import ax.business.Product;
import ax.repositories.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSpringBootDataSolrApplication {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void applicationAndSolrBootstraps() {
        assertNotNull(productRepository);
        assertEquals(32, productRepository.count());

        Product product = productRepository.findOne("TWINX2048-3200PRO");
        assertNotNull(product);
        assertEquals("TWINX2048-3200PRO", product.getId());
        assertEquals("CORSAIR  XMS 2GB (2 x 1GB) 184-Pin DDR SDRAM Unbuffered DDR 400 (PC 3200) Dual Channel Kit System Memory - Retail", product.getName());
        assertTrue(product.isInStock());
        assertEquals("Corsair Microsystems Inc.", product.getManu());
        assertEquals(185, product.getPrice(), 0);
        assertEquals(5, product.getPopularity());
        assertEquals("corsair", product.getManufacturerId());

        assertEquals(3, productRepository.countByManufacturerId("corsair"));
    }
}
