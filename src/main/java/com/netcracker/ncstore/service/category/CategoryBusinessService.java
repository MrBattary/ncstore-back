package com.netcracker.ncstore.service.category;

import com.netcracker.ncstore.exception.CategoryServiceNotFoundException;
import com.netcracker.ncstore.model.Category;
import com.netcracker.ncstore.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CategoryBusinessService implements ICategoryBusinessService {
    private final CategoryRepository categoryRepository;

    public CategoryBusinessService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Category getCategoryByName(String name) throws CategoryServiceNotFoundException {
        return null;
    }

    @Override
    public List<Category> getCategoriesForProduct(UUID productId) {
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
