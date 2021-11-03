package com.netcracker.ncstore.service;

import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.ProductPriceInRegionDTO;

public interface IPricesService {
    public ProductPriceInRegionDTO getPriceForProductInRegion(ProductLocaleDTO productLocale);
}
