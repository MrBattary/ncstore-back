package com.netcracker.ncstore.service.category;

import com.netcracker.ncstore.exception.CategoryNotFoundException;
import com.netcracker.ncstore.model.Category;
import com.netcracker.ncstore.repository.CategoryRepository;
import com.netcracker.ncstore.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;
    private final Logger log;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        log = LoggerFactory.getLogger(UserService.class);
    }

    @Override
    public Category getCategoryEntityByName(String name) throws CategoryNotFoundException {
        Category category = categoryRepository.findByName(name);

        if(category==null) {
            throw new CategoryNotFoundException();
        }

        return category;
    }
}
