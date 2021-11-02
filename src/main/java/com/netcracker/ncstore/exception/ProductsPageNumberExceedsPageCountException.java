package com.netcracker.ncstore.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductsPageNumberExceedsPageCountException extends RuntimeException {
    private int requestedPageNumber = -1;
    private int totalPageCount = -1;
}
