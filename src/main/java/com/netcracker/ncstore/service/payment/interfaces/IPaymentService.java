package com.netcracker.ncstore.service.payment.interfaces;

import com.netcracker.ncstore.dto.PaymentProceedDTO;
import com.netcracker.ncstore.exception.PaymentServiceException;

/**
 * basic interface for all payment services
 */
public interface IPaymentService {
    /**
     * method to get token
     *
     * @return payment token
     */
    String getPaymentToken();

    /**
     * Proceeds payment and returns transaction ID
     *
     * @param paymentProceedDTO DTO containing info about payment
     * @return transaction id
     * @throws PaymentServiceException when there is some exception during payment
     */
    String proceedPaymentInRealMoney(PaymentProceedDTO paymentProceedDTO) throws PaymentServiceException;

}
