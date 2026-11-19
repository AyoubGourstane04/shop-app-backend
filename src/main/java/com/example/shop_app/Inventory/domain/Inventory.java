package com.example.shop_app.Inventory.domain;

import com.example.shop_app.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "inventory")
@NoArgsConstructor
@Getter
@Setter
public class Inventory extends BaseEntity {
    @Column(name = "product_id", unique = true, nullable = false)
    private UUID productId;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity = 0;

    public Inventory(UUID productId, int availableQuantity){
        this.productId = productId;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = 0;
    }

}
