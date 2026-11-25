package com.example.shop_app.catalog.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "SKU is required") String sku,
        @NotBlank(message = "Name is required") String name,
        String description,
        @NotNull(message = "Price is required") BigDecimal price,
        String currency,
        String imageUrl,
        String categoryId,
        Boolean isFeatured
        ) {
}
