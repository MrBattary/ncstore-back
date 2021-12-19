package com.netcracker.ncstore.service.payment;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.MerchantAccount;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.netcracker.ncstore.dto.PaymentProceedDTO;
import com.netcracker.ncstore.exception.PaymentServiceCurrencyNotSupportedException;
import com.netcracker.ncstore.exception.PaymentServiceException;
import com.netcracker.ncstore.service.payment.interfaces.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Iterator;

@Service
@Slf4j
public class BraintreePaymentService implements IPaymentService {
    private final BraintreeGateway braintreeGateway;

    public BraintreePaymentService(final BraintreeGateway braintreeGateway) {
        this.braintreeGateway = braintreeGateway;
    }

    @Override
    public String getPaymentToken() {
        return braintreeGateway.clientToken().generate();
    }

    @Override
    public String proceedPaymentInRealMoney(PaymentProceedDTO paymentProceedDTO) {
        log.info("Processing payment with amount " + paymentProceedDTO.getAmount() + " in currency with ISO code " + Currency.getInstance(paymentProceedDTO.getRegion()).getCurrencyCode());

        String currencyISOCode = Currency.getInstance(paymentProceedDTO.getRegion()).getCurrencyCode();
        Iterator<MerchantAccount> merchantAccountIterator = braintreeGateway.merchantAccount().all().iterator();
        MerchantAccount paymentMerchantAccount = null;

        while (merchantAccountIterator.hasNext()) {
            MerchantAccount merchantAccount = merchantAccountIterator.next();
            if (merchantAccount.getCurrencyIsoCode().equals(currencyISOCode)) {
                paymentMerchantAccount = merchantAccount;
            }
        }
        if (paymentMerchantAccount == null) {
            log.error("There was a request to pay in currency" + currencyISOCode + " but payment in that currency is not suppoerted");
            throw new PaymentServiceCurrencyNotSupportedException("Currency with code " + currencyISOCode + " not supported");
        }

        TransactionRequest request = new TransactionRequest()
                .amount(paymentProceedDTO.getAmount())
                .paymentMethodNonce(paymentProceedDTO.getNonce())
                .merchantAccountId(paymentMerchantAccount.getId())
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = braintreeGateway.transaction().sale(request);
        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();
            return transaction.getId();
        } else {
            StringBuilder errorStringBuilder = new StringBuilder();
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                errorStringBuilder.append("Error: ").append(error.getCode()).append(": ").append(error.getMessage()).append("\n");
            }
            log.error("Can not proceed payment due to error");
            throw new PaymentServiceException(errorStringBuilder.toString());
        }
    }
}
