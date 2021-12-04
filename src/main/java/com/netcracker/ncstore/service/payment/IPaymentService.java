package com.netcracker.ncstore.service.payment;

import com.netcracker.ncstore.dto.PaymentProceedDTO;
import com.netcracker.ncstore.dto.response.PaymentGetResponse;

/**
 * basic interface for all payment services
 */
public interface IPaymentService {
    /**
     * method to get token
     * @return payment token
     */
    String getPaymentToken();

    /**
     * Proceeds payment and returns transaction ID
     *
     * @param paymentProceedDTO DTO containing info about payment
     * @return transaction id
     */
    String proceedPayment(PaymentProceedDTO paymentProceedDTO);

}
