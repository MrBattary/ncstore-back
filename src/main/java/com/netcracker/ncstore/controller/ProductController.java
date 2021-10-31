package com.netcracker.ncstore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Product controller is responsible for any actions with products
 */
@RestController
public class ProductController {
    private final Logger log;

    /**
     * Constructor
     *
     * TODO: In the future, any services should be the arguments of constructor
     */
    public ProductController() {
        this.log = LoggerFactory.getLogger(ProductController.class);
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/getProducts
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getProductsWithPagination(@RequestParam final Integer page,
                                                       @RequestParam final Integer size) {
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/createProduct
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> createProduct() {
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/getProduct
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getProduct(@PathVariable final String productId){
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/updateProduct
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> updateProduct(@PathVariable final String productId){
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/deleteProduct
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deleteProduct(@PathVariable final String productId){
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/buyProduct

    @RequestMapping(value = "/products/{productId}/buy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> buyProduct(@PathVariable final String productId){
        return null;
    }
}
