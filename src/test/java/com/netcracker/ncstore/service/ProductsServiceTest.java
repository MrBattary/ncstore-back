package com.netcracker.ncstore.service;

import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class ProductsServiceTest {

    @Autowired
    ProductsService productsService;

    @MockBean
    ProductRepository productRepository;
    @MockBean
    PricesService pricesService;

    @Test
    void getPageOfProductsByNameAndCategories() {
        String searchText = "test";
        List<UUID> categories = new ArrayList<>();
        categories.add(UUID.randomUUID());
        categories.add(UUID.randomUUID());
        categories.add(UUID.randomUUID());

        Pageable pageable = PageRequest.of(1,2);
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        products.add(new Product());
        products.add(new Product());
        products.add(new Product());
        Page<Product> productPage = new PageImpl<>(products);

        Mockito.when(productRepository.findProductsByLikeNameAndCategories(searchText, categories, pageable))
                .thenReturn(productPage);


    }
}