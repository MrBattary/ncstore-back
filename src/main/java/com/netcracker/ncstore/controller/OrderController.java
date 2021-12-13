package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.request.OrderGetRequest;
import com.netcracker.ncstore.dto.request.OrderInfoGetRequest;
import com.netcracker.ncstore.dto.response.OrderGetResponse;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.service.order.interfaces.IOrderWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * Order controller is responsible for any actions with orders
 */
@RestController
@RequestMapping(value = "/orders")
@Slf4j
public class OrderController {

    private final IOrderWebService orderWebService;

    public OrderController(final IOrderWebService orderWebService) {

        this.orderWebService = orderWebService;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.4#/Order/getOrders
    @GetMapping
    public ResponseEntity<List<OrderGetResponse>> getOrdersWithPagination(@RequestParam final int page,
                                                                          @RequestParam final int size,
                                                                          final Principal principal) {
        OrderGetRequest request = new OrderGetRequest(page, size, principal.getName());
        log.info("REQUEST: to get orders for user with email" + principal.getName() + " on page: " + page + " with size: " + size);

        List<OrderGetResponse> response = orderWebService.getOrders(request);

        log.info("RESPONSE: to get orders for user with email" + principal.getName() + " on page: " + page + " with size: " + size);

        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.4#/Order/getOrder
    @GetMapping(value = "/{orderId}")
    public ResponseEntity<OrderInfoResponse> getOrder(@PathVariable final UUID orderId, Principal principal) {
        OrderInfoGetRequest request = new OrderInfoGetRequest(orderId, principal.getName());

        OrderInfoResponse response = orderWebService.getSpecificOrder(request);

        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }
}
