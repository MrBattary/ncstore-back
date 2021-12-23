package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.response.PaymentGetResponse;
import com.netcracker.ncstore.service.general.payment.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<PaymentGetResponse> getPaymentToken(Principal principal) {
        log.info("REQUEST: to get payment token for user with email " + principal.getName());

        String clientToken = paymentService.getPaymentToken();

        PaymentGetResponse response = new PaymentGetResponse(clientToken);

        log.info("RESPONSE: to get payment token for user with email " + principal.getName());

        return ResponseEntity.
                ok().
                body(response);
    }
}
