package com.netcracker.ncstore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Order controller is responsible for any actions with orders
 */
@RestController
public class OrderController {
    private final Logger log;

    /**
     * Constructor
     * <p>
     * TODO: In the future, any services should be the arguments of constructor
     */
    public OrderController() {
        this.log = LoggerFactory.getLogger(OrderController.class);
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Order/getOrders
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getOrdersWithPagination(@RequestParam final Integer page,
                                                     @RequestParam final Integer size) {
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Order/getOrder
    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getOrder(@PathVariable final String orderId) {
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Order/refundItemsOrder
    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> refundFromOrder(@PathVariable final String orderId) {
        return null;
    }
}
