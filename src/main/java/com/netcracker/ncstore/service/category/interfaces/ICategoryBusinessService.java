package com.netcracker.ncstore.service.category.interfaces;

import com.netcracker.ncstore.exception.CategoryServiceNotFoundException;
import com.netcracker.ncstore.model.Category;

import java.util.List;
import java.util.UUID;

/**
 * Interface for all business services that work with Category Entity
 */
public interface ICategoryBusinessService {
    /**
     * Return real Category entity by name
     *
     * @param name - the name of Category
     * @return Category
     * @throws CategoryServiceNotFoundException - when no category with provided name exists
     */
    Category getCategoryByName(final String name) throws CategoryServiceNotFoundException;

    /**
     * Return list of Categories for product with provided ID
     *
     * @param productId - the UUID of product
     * @return List of Category entities
     */
    List<Category> getCategoriesForProduct(final UUID productId);

    /**
     * Returns all categories form database
     *
     * @return List of all categories
     */
    List<Category> getAllCategories();
}
