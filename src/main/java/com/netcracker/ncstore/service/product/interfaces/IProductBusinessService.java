package com.netcracker.ncstore.service.product.interfaces;

import com.netcracker.ncstore.dto.ProductCreateDTO;
import com.netcracker.ncstore.dto.ProductDiscontinueDTO;
import com.netcracker.ncstore.dto.ProductUpdateDTO;
import com.netcracker.ncstore.exception.ProductServicePermissionException;
import com.netcracker.ncstore.exception.ProductServiceNotFoundException;
import com.netcracker.ncstore.exception.ProductServiceNotFoundExpectedException;
import com.netcracker.ncstore.exception.ProductServiceValidationException;
import com.netcracker.ncstore.model.Product;

/**
 * Interface for services responsible for creating, updating and deleting products
 */
public interface IProductBusinessService {
    /**
     * Create new product in store with given data.
     *
     * @param productCreateDTO DTO containing all needed information
     * @return Product created product entity
     * @throws ProductServiceValidationException if provided data is not valid
     */
    Product createProduct(final ProductCreateDTO productCreateDTO)
            throws ProductServiceValidationException;

    /**
     * Update existing product
     *
     * @param productUpdateDTO - DTO containing info for updating product
     * @return updated Product entity
     * @throws ProductServiceNotFoundException   if product was not found
     * @throws ProductServicePermissionException if product does not belong to requesting supplier
     * @throws ProductServiceValidationException if provided data is not valid
     */
    Product updateExistingProduct(ProductUpdateDTO productUpdateDTO)
            throws ProductServiceNotFoundException, ProductServicePermissionException, ProductServiceValidationException;

    /**
     * Delete product from store which means setting its status to DISCONTINUED and removing all prices.
     *
     * @param productDiscontinueDTO DTO containing info for product deletion
     * @return deleted product entity
     * @throws ProductServiceNotFoundExpectedException if product was not found
     * @throws ProductServicePermissionException       if product does not belong to requesting supplier
     */
    Product discontinueProductSales(ProductDiscontinueDTO productDiscontinueDTO)
            throws ProductServiceNotFoundExpectedException, ProductServicePermissionException;
}
