package com.example.shop_app.inventory.domain;

import java.util.UUID;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(UUID productId, int requested) {
        super("Insufficient stock for product " + productId + ". Requested quantity: " + requested);
    }
}
