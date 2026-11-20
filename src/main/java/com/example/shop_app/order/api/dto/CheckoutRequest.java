package com.example.shop_app.order.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CheckoutRequest(
        @NotNull(message = "Customer ID is required") UUID customerId,
        String currency,
        @NotNull(message = "Order must contain at least one item") List<OrderItemRequest> items
){}
