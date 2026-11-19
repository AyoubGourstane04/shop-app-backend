package com.example.shop_app.catalog.api.dto;

import com.example.shop_app.catalog.domain.Product;
import com.example.shop_app.catalog.domain.ProductStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        String currency,
        ProductStatus status,
        Instant createdAt
) {
    public static ProductResponse fromEntity(Product p){
        return new ProductResponse(
                p.getId(),
                p.getSku(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getCurrency(),
                p.getStatus(),
                p.getCreatedAt()
        );
    }
}
