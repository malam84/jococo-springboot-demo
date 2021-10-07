package com.jococo.springboot.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.jococo.springboot.demo.model.Product;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {
	
	 private static Product p1;
	
	@Autowired
    private ProductRepository repository;
	
	@Test
    @Rollback(value = false)
    void createNewProduct_Test() {
		p1 = new Product("P1", "Desktop", 100);
		Product product = repository.save(p1);
        assertNotNull(product);
    }
	
	@Test
    void findProductlist_Test() {
        List<Product> prodList = repository.findAll();
        assertThat(prodList).size().isGreaterThan(0);
    }
	
	@Test
    void findProductById_Test() {
        Optional<Product> user = repository.findById(1L);
        assertThat(user.orElse(null)).isNotNull();
    }
	
	@Test
    void deleteProductById_Test() {
       boolean isPresentBeforeDelete = repository.findById(1L).isPresent();
       repository.deleteById(1L);
       boolean notPresentAfterDelete = repository.findById(1L).isPresent();

       assertTrue(isPresentBeforeDelete);
       assertFalse(notPresentAfterDelete);
    }
}
