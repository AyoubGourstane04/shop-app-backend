package com.example.shop_app.inventory.api;

import com.example.shop_app.inventory.application.InventoryService;
import com.example.shop_app.inventory.domain.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/{productId}/initialize")
    @ResponseStatus(HttpStatus.CREATED)
    public void initialize(@PathVariable UUID productId, @RequestParam int quantity){
        inventoryService.initialize(productId, quantity);
    }

    @PostMapping("/{productId}/reserve")
    @ResponseStatus(HttpStatus.OK)
    public void reserve(@PathVariable UUID productId, @RequestParam int quantity){
        inventoryService.reserveStock(productId, quantity);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.FOUND)
    public Inventory getStock(@PathVariable UUID productId){
        return inventoryService.getStock(productId);
    }


}
