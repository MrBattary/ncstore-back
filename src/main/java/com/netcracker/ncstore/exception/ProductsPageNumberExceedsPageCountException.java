package com.netcracker.ncstore.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This exception is used when requested page number is too big
 * and exceeds max number of available pages.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductsPageNumberExceedsPageCountException extends RuntimeException {
    private int requestedPageNumber = -1;
    private int totalPageCount = -1;
}
