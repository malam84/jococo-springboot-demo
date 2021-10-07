package com.jococo.springboot.demo.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.jococo.springboot.demo.model.Product;
import com.jococo.springboot.demo.repository.ProductRepository;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ProductServiceTest {

    private static Product p1;
    private static Product p2;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeAll
    public static void init() {
        p1 = new Product("P1", "Desktop", 100);
        p2 = new Product("P2", "Laptop", 200);
    }

    @Test
    public void findAllProduct_Test() {
        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));
        assertThat(productService.findAll().size(), is(2));
        assertThat(productService.findAll().get(0), is(p1));
        assertThat(productService.findAll().get(1),is(p2));
        Mockito.verify(productRepository, Mockito.times(3)).findAll();
    }

    @Test
    public void findProductById_Test() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(p1));
        assertThat(productService.findById(1L), is(Optional.of(p1)));
        Mockito.verify(productRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void createOrUpdateProduct_Test() {
        Mockito.when(productRepository.save(p1)).thenReturn(p1);
        assertThat(productService.createOrUpdate(p1), is(p1));
        Mockito.verify(productRepository, Mockito.times(1)).save(p1);

        Mockito.when(productRepository.save(p2)).thenReturn(p2);
        assertThat(productService.createOrUpdate(p2).getName(), is("P2"));
        Mockito.verify(productRepository, Mockito.times(1)).save(p2);
    }

    @Test
    void deleteProductById_Test() {
        productService.deleteById(1L);
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(1L);
    }
}