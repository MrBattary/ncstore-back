package com.netcracker.ncstore.service.category;

import com.netcracker.ncstore.dto.data.CategoryDTO;
import com.netcracker.ncstore.exception.CategoryServiceNotFoundException;
import com.netcracker.ncstore.model.Category;

import java.util.List;
import java.util.UUID;

/**
 * This interface must be implemented by all services that
 * work with logic related to category entity
 */
public interface ICategoryService {
    /**
     * Return real Category entity by name
     * @param name - the name of Category
     * @return Category
     * @throws CategoryServiceNotFoundException - when no category with provided name exists
     */
    Category getCategoryEntityByName(final String name) throws CategoryServiceNotFoundException;

    /**
     * Return list of Categories DTOs for provided product
     * @param productId - the UUID of product
     * @return List of CategoryDTOs
     */
    List<CategoryDTO> getCategoriesForProduct(final UUID productId);
}
