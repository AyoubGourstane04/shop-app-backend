package com.example.shop_app.Inventory.application;

import com.example.shop_app.Inventory.domain.InsufficientStockException;
import com.example.shop_app.Inventory.domain.Inventory;
import com.example.shop_app.Inventory.infrastructure.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional
    public void initialize(UUID productId, int quantity){
        Inventory inventory = new Inventory(productId, quantity);
        inventoryRepository.save(inventory);
        log.info("Initialized inventory for product {} with {} units", productId, quantity);
    }


    @Transactional
    public void reserveStock(UUID productId, int quantity){
        int updatedRows = inventoryRepository.reserveStock(productId, quantity);

        if(updatedRows == 0) {
            log.warn("Failed to reserve {} units for product {}: Insufficient stock", quantity, productId);
            throw new InsufficientStockException(productId, quantity);
        }
        log.info("Successfully reserved {} units for product {}", quantity, productId);
    }

    @Transactional
    public void releaseStock(UUID productId, int quantity){
        int updatedRows = inventoryRepository.releaseStock(productId, quantity);

        if(updatedRows == 0) {
            log.error("Failed to release {} units for product {}: Inconsistent state", quantity, productId);
            throw new IllegalStateException("Could not release stock for product " + productId);
        }
        log.info("Released {} units back to available stock for product {}", quantity, productId);
    }

    @Transactional
    public void commitStock(UUID productId, int quantity){
        int updatedRows = inventoryRepository.commitStock(productId, quantity);

        if(updatedRows == 0) {
            log.error("Failed to commit {} units for product {}: Inconsistent state", quantity, productId);
            throw new IllegalStateException("Could not commit stock for product " + productId);
        }
        log.info("Permanently deducted {} units for product {}", quantity, productId);
    }

    @Transactional(readOnly = true)
    public Inventory getStock(UUID productId){
        return inventoryRepository.findByProductId(productId).orElseThrow(
                () -> new IllegalArgumentException("No inventory record for product " + productId)
        );
    }



}
