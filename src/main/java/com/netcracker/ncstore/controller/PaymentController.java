package com.netcracker.ncstore.controller;

import com.braintreegateway.BraintreeGateway;
import com.netcracker.ncstore.dto.PaymentProceedDTO;
import com.netcracker.ncstore.dto.request.PaymentPostRequest;
import com.netcracker.ncstore.dto.response.PaymentGetResponse;
import com.netcracker.ncstore.dto.response.PaymentPostResponse;
import com.netcracker.ncstore.service.payment.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping(value = "/payment")
public class PaymentController {

    private final IPaymentService paymentService;

    public PaymentController(final IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<PaymentGetResponse> getPaymentToken(){
        String clientToken = paymentService.getPaymentToken();

        PaymentGetResponse response = new PaymentGetResponse(clientToken);

        return ResponseEntity.
                ok().
                body(response);
    }
}
