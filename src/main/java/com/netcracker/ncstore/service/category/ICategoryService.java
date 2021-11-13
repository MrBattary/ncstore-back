package com.netcracker.ncstore.service.category;

import com.netcracker.ncstore.exception.CategoryNotFoundException;
import com.netcracker.ncstore.model.Category;

/**
 * This interface must be implemented by all services that
 * work with logic related to category entity
 */
public interface ICategoryService {
    /**
     * Return real Category entity by name
     * @param name - the name of Category
     * @return Category
     * @throws CategoryNotFoundException - when no category with provided name exists
     */
    Category getCategoryEntityByName(String name) throws CategoryNotFoundException;
}
