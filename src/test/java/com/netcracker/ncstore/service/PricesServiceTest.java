package com.netcracker.ncstore.service;

import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.repository.ProductPriceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

class PricesServiceTest {
    @Mock
    private ProductPriceRepository productPriceRepository;

    @Test
    void getPriceForProductInRegion() {
        ProductPrice productPriceMocked = Mockito.mock(ProductPrice.class);
        doReturn(productPriceMocked).when(productPriceRepository.findByProductIDAndLocale(any(), any()));
    }
}