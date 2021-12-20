package com.netcracker.ncstore.service.category;

import com.netcracker.ncstore.dto.response.CategoryGetResponse;
import com.netcracker.ncstore.service.category.interfaces.ICategoryDataService;
import com.netcracker.ncstore.service.category.interfaces.ICategoryWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryWebService implements ICategoryWebService {
    private final ICategoryDataService categoryDataService;

    public CategoryWebService(ICategoryDataService categoryDataService) {
        this.categoryDataService = categoryDataService;
    }

    @Override
    public List<CategoryGetResponse> getAllCategoriesResponse() {
        return categoryDataService.getAllCategories().
                stream().
                map(e -> new CategoryGetResponse(
                                e.getId(),
                                e.getName()
                        )
                ).
                collect(Collectors.toList());
    }
}
