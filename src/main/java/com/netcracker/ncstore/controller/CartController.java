package com.netcracker.ncstore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/cart")
public class CartController {
    public CartController() {

    }

    @GetMapping
    public ResponseEntity<?> getShoppingCartProducts() {

        return null;
    }

    @PutMapping
    public ResponseEntity<?> addProductToShoppingCart() {

        return null;
    }

    @PostMapping
    public ResponseEntity<?> checkout() {

        return null;
    }
}
