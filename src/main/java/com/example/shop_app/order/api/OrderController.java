package com.example.shop_app.order.api;

import com.example.shop_app.order.api.dto.CheckoutRequest;
import com.example.shop_app.order.api.dto.OrderResponse;
import com.example.shop_app.order.application.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse checkout(@RequestHeader("Idempotency-Key") String idempotencyKey, @Valid @RequestBody CheckoutRequest request){
        return orderService.processCheckout(idempotencyKey, request);
    }

}
