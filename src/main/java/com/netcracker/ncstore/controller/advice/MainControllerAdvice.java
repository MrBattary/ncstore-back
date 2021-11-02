package com.netcracker.ncstore.controller.advice;

import com.netcracker.ncstore.exception.ProductsPageNumberExceedsPageCountException;
import com.netcracker.ncstore.exception.RequestParametersInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MainControllerAdvice {
    @ExceptionHandler(value = {RequestParametersInvalidException.class})
    public ResponseEntity<?> handleRequestParametersInvalidException(){
        return new ResponseEntity<>(
                "Request parameters are invalid. Check API for making a correct request.",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ProductsPageNumberExceedsPageCountException.class})
    public ResponseEntity<?> handleProductsPageNumberExceedsPageCountException(){
        return new ResponseEntity<>(
                "Provided page number for provide page size exceeds page count. There is no such page.",
                HttpStatus.NOT_FOUND);
    }
}
