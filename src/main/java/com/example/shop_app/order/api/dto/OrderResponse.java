package com.example.shop_app.order.api.dto;

import com.example.shop_app.order.domain.Order;
import com.example.shop_app.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    String orderNumber,
    UUID customerId,
    OrderStatus status,
    BigDecimal totalAmount,
    String currency,
    List<OrderItemResponse> items,
    Instant createdAt
) {
    public static OrderResponse fromEntity(Order order){
        List<OrderItemResponse> itemResponses = order.getItems()
                .stream()
                .map(i -> new OrderItemResponse(
                        i.getProductId(),
                        i.getProductName(),
                        i.getUnitPrice(),
                        i.getQuantity()
                )).toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCurrency(),
                itemResponses,
                order.getCreatedAt()
        );
    }
}
