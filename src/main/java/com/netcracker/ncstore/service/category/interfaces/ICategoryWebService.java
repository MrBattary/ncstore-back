package com.netcracker.ncstore.service.category.interfaces;

import com.netcracker.ncstore.dto.response.CategoryGetResponse;

import java.util.List;

/**
 * Interface for all WEB services that work with category
 */
public interface ICategoryWebService {
    /**
     * Returns List of all categories
     *
     * @return DTO containing HTTP response for category GET
     */
    List<CategoryGetResponse> getAllCategoriesResponse();
}
