package com.example.shop_app.payment.application;

import com.example.shop_app.Inventory.application.InventoryService;
import com.example.shop_app.order.domain.Order;
import com.example.shop_app.order.domain.OrderItem;
import com.example.shop_app.order.domain.OrderStatus;
import com.example.shop_app.order.infrastructure.OrderRepository;
import com.example.shop_app.payment.domain.Payment;
import com.example.shop_app.payment.domain.PaymentStatus;
import com.example.shop_app.payment.infrastructure.ExternalPaymentGatewayClient;
import com.example.shop_app.payment.infrastructure.PaymentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final ExternalPaymentGatewayClient gatewayClient;

    @Transactional
    @CircuitBreaker(name = "paymentGateway", fallbackMethod = "paymentFallback")
    public Payment processPayment(UUID orderId, String idempotencyKey, String paymentMethod){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        Payment payment = new Payment(
                order.getId(),
                idempotencyKey,
                order.getTotalAmount(),
                order.getCurrency(),
                paymentMethod
        );

        ExternalPaymentGatewayClient.GatewayResponse response = gatewayClient.charge(
                payment.getAmount(),
                payment.getCurrency(),
                payment.getPaymentMethod()
        );

        if(response.successful()){
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setExternalTransactionId(response.transactionId());
            order.setStatus(OrderStatus.PAID);

            for(OrderItem item : order.getItems()){
                inventoryService.commitStock(item.getProductId(), item.getQuantity());
            }

            log.info("Saga Complete: Order {} is fully PAID and stock committed.", order.getOrderNumber());
        }else{
            executeSagaCompensation(order, payment, response.errorMessage());
        }

        paymentRepository.save(payment);
        orderRepository.save(order);

        return payment;
    }

    // SAGA COMPENSATING TRANSACTION: Undoes the inventory reservation and marks order as CANCELLED.
    private void executeSagaCompensation(Order order, Payment payment, String reason) {
        log.warn("Saga Compensation Triggered for Order {}. Reason: {}", order.getOrderNumber(), reason);
        payment.setStatus(PaymentStatus.FAILED);
        order.setStatus(OrderStatus.CANCELLED);

        for(OrderItem item : order.getItems()){
            inventoryService.releaseStock(item.getProductId(), item.getQuantity());
        }

        log.info("Saga Compensation Complete: Released stock back to inventory for Order {}.", order.getOrderNumber());
    }

    // Resilience4j Fallback method (called when Circuit Breaker is OPEN or throws an unhandled exception).
    public Payment paymentFallback(UUID orderId, String idempotencyKey, String paymentMethod, Throwable ex){
        log.error("Circuit Breaker OPEN or Payment Gateway error: {}", ex.getMessage());

        Order order = orderRepository.findById(orderId).orElseThrow();
        Payment payment = new Payment(
                order.getId(),
                idempotencyKey,
                order.getTotalAmount(),
                order.getCurrency(),
                paymentMethod
        );

        executeSagaCompensation(order, payment, "Payment service temporarily unavailable (Circuit Breaker open)");
        paymentRepository.save(payment);
        orderRepository.save(order);

        return payment;


    }
}
