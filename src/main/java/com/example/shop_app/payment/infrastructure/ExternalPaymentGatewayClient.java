package com.example.shop_app.payment.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
public class ExternalPaymentGatewayClient {

    public record GatewayResponse(boolean successful, String transactionId, String errorMessage){}

    public GatewayResponse charge(BigDecimal amount, String currency, String paymentMethod){
        log.info("Contacting external payment gateway for amount: {} {} via {}", amount, currency, paymentMethod);

        if("FAIL_CARD".equalsIgnoreCase(paymentMethod)){
            log.warn("Payment declined by gateway: Insufficient funds or invalid card.");
            return new GatewayResponse(false, null, "Card declined by issuing bank");
        }

        if("TIMEOUT".equalsIgnoreCase(paymentMethod)){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}
        }

        String txId = "tx_" + UUID.randomUUID().toString().replace("-", "").substring(0,16);
        log.info("Payment successful! Gateway transaction ID: {}", txId);
        return new GatewayResponse(true, txId, null);
    }





}
