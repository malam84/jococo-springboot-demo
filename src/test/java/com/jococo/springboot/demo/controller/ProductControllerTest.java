package com.jococo.springboot.demo.controller;

import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.jococo.springboot.demo.model.Product;
import com.jococo.springboot.demo.service.ProductService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
class ProductControllerTest {

	@MockBean
    private ProductService productService;

    @Autowired                           
    private MockMvc mockMvc; 
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private List<Product> prodList; 

    @BeforeEach                           
    void setUp() {                               
       this.prodList = new ArrayList<>();                                    
       this.prodList.add(new Product("P1", "Dummy Product", 100)); 
       this.prodList.add(new Product("P2", "Desktop", 200));   
       this.prodList.add(new Product("P3", "Laptop", 300));                                                       
    }
    
    @Test
    void findAllProductList_Test() throws Exception {
    	given(productService.findAll()).willReturn(prodList);
    	this.mockMvc.perform(get("/api/v1/products"))
        	.andExpect(status().isOk())
        	.andExpect(jsonPath("$.size()", is(prodList.size())));
    }
    
    @Test
    void findProductById_Test() throws Exception {
    	Long prodId = 1L;
        Product prod = new Product("P1", "Dummy Product", 100);
        given(productService.findById(prodId)).willReturn(Optional.of(prod));
        this.mockMvc.perform(get("/api/v1/products/{id}", prodId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(prod.getName())))
                .andExpect(jsonPath("$.description", is(prod.getDescription())))
                .andExpect(jsonPath("$.price", is(prod.getPrice())));
    }
    
    @Test
    void createNewProduct_Test() throws Exception {
    	given(productService.createOrUpdate(any(Product.class))).willAnswer((invocation) -> invocation.getArgument(0));
    	Product product = new Product("p4", "Dummy Product2", 400);
        this.mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$.price", is(product.getPrice())));
    }

}