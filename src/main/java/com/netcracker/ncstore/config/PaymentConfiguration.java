package com.netcracker.ncstore.config;

import com.braintreegateway.BraintreeGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used to configure anything related to payment
 */
@Configuration
public class PaymentConfiguration {

    @Value("${payment.braintree.environment}")
    private String BT_ENVIRONMENT;

    @Value("${payment.braintree.merchant_id}")
    private String BT_MERCHANT_ID;

    @Value("${payment.braintree.public_key}")
    private String BT_PUBLIC_KEY;

    @Value("${payment.braintree.private_key}")
    private String BT_PRIVATE_KEY;


    @Bean
    public BraintreeGateway braintreeGateway() {
        return new BraintreeGateway(
                BT_ENVIRONMENT,
                BT_MERCHANT_ID,
                BT_PUBLIC_KEY,
                BT_PRIVATE_KEY
        );
    }
}
