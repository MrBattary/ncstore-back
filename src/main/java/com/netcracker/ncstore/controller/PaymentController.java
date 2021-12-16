package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.response.PaymentGetResponse;
import com.netcracker.ncstore.service.payment.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/payment")
public class PaymentController {

    private final IPaymentService paymentService;

    public PaymentController(final IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<PaymentGetResponse> getPaymentToken() {
        String clientToken = paymentService.getPaymentToken();

        PaymentGetResponse response = new PaymentGetResponse(clientToken);

        return ResponseEntity.
                ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
