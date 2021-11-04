package com.netcracker.ncstore.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This exception is used when requested page number is too big
 * and exceeds max number of available pages.
 */
@NoArgsConstructor
@AllArgsConstructor
public class ProductsPageNumberExceedsPageCountException extends RuntimeException {
    private int requestedPageNumber = -1;
    private int totalPageCount = -1;


    @Override
    public String getMessage() {
        if (requestedPageNumber != -1 && totalPageCount != -1) {
            return "Requested page: " + requestedPageNumber + "\n" +
                    "Total page count: " + totalPageCount;
        } else {
            return "Requested page exceeds total pages count";
        }
    }
}
