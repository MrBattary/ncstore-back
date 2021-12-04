package com.netcracker.ncstore.service.payment;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.netcracker.ncstore.dto.PaymentProceedDTO;
import com.netcracker.ncstore.exception.PaymentServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BraintreePaymentService implements IPaymentService{
    private final BraintreeGateway braintreeGateway;

    public BraintreePaymentService(final BraintreeGateway braintreeGateway) {
        this.braintreeGateway = braintreeGateway;
    }

    @Override
    public String getPaymentToken() {
        return braintreeGateway.clientToken().generate();
    }

    @Override
    public String proceedPayment(PaymentProceedDTO paymentProceedDTO) {
        TransactionRequest request = new TransactionRequest()
                .amount(paymentProceedDTO.getAmount())
                .paymentMethodNonce(paymentProceedDTO.getNonce())
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = braintreeGateway.transaction().sale(request);
        if(result.isSuccess()){
            Transaction transaction = result.getTarget();
            return transaction.getId();
        }else{
            StringBuilder errorStringBuilder = new StringBuilder();
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                errorStringBuilder.append("Error: ").append(error.getCode()).append(": ").append(error.getMessage()).append("\n");
            }
            throw new PaymentServiceException(errorStringBuilder.toString());
        }
    }
}
