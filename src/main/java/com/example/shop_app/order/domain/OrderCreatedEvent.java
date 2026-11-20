package com.example.shop_app.order.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        String OrderNumber,
        UUID customerId,
        BigDecimal totalAmount,
        String currency,
        Instant createdAt
) {}
