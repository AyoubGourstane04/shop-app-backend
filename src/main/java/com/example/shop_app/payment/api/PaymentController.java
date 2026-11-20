package com.example.shop_app.payment.api;

import com.example.shop_app.payment.application.PaymentService;
import com.example.shop_app.payment.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    public record PayOrderRequest(String paymentMethod){}

    @PostMapping("/{orderId}")
    public Payment payOrder(
            @PathVariable UUID orderId,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody PayOrderRequest request
    ){
        return paymentService.processPayment(orderId, idempotencyKey, request.paymentMethod());
    }



}
