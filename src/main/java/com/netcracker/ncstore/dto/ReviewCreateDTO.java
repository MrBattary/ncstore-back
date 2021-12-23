package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewCreateDTO {
    private final User author;
    private final Product product;
    private final int rating;
    private final String text;
}
