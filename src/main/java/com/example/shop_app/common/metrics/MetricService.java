package com.example.shop_app.common.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricService {

    private final Counter orderCreatedCounter;
    private final Counter paymentsFailedCounter;

    public MetricService(MeterRegistry registry){
        this.orderCreatedCounter = Counter.builder("shop.orders.created.total")
                .description("Total number of orders successfully placed")
                .register(registry);

        this.paymentsFailedCounter = Counter.builder("shop.payments.failed.total")
                .description("Total number of failed payment attempts")
                .register(registry);
    }

    public void incrementOrdersCreated(){
        this.orderCreatedCounter.increment();
    }

    public void incrementPaymentsFailed(){
        this.paymentsFailedCounter.increment();
    }

}
