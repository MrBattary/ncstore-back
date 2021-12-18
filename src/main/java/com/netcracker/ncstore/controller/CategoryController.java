package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.response.CategoryGetResponse;
import com.netcracker.ncstore.service.category.interfaces.ICategoryWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/category")
@Slf4j
public class CategoryController {

    private final ICategoryWebService categoryWebService;

    public CategoryController(ICategoryWebService categoryWebService) {
        this.categoryWebService = categoryWebService;
    }


    @GetMapping
    public ResponseEntity<List<CategoryGetResponse>> getCategories() {
        log.info("REQUEST: to get categories");

        List<CategoryGetResponse> response = categoryWebService.getAllCategoriesResponse();

        log.info("RESPONSE: to get categories");

        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }
}
