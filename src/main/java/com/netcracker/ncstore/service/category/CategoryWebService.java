package com.netcracker.ncstore.service.category;

import com.netcracker.ncstore.dto.response.CategoryGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryWebService implements ICategoryWebService {
    private final ICategoryBusinessService categoryBusinessService;

    public CategoryWebService(ICategoryBusinessService categoryBusinessService) {
        this.categoryBusinessService = categoryBusinessService;
    }

    @Override
    public List<CategoryGetResponse> getAllCategoriesResponse() {
        return categoryBusinessService.getAllCategories().
                stream().
                map(e -> new CategoryGetResponse(
                                e.getId(),
                                e.getName()
                        )
                ).
                collect(Collectors.toList());
    }
}
