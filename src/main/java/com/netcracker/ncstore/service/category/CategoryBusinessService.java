package com.netcracker.ncstore.service.category;

import com.netcracker.ncstore.exception.CategoryServiceNotFoundException;
import com.netcracker.ncstore.model.Category;
import com.netcracker.ncstore.repository.CategoryRepository;
import com.netcracker.ncstore.service.category.interfaces.ICategoryBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryBusinessService implements ICategoryBusinessService {
    private final CategoryRepository categoryRepository;

    public CategoryBusinessService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Category getCategoryByName(String name) throws CategoryServiceNotFoundException {
        return categoryRepository.
                findByName(name).
                orElseThrow(
                        () -> new CategoryServiceNotFoundException("Unable to find category with name " + name)
                );
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
