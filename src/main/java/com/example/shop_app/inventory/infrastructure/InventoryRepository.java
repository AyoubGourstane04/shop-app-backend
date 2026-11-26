package com.example.shop_app.inventory.infrastructure;

import com.example.shop_app.inventory.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    Optional<Inventory> findByProductId(UUID productId);

    List<Inventory> findByProductIdIn(Collection<UUID> productIds);

    // ATOMIC RESERVE
    @Modifying
    @Query("""
        UPDATE Inventory i
        SET i.availableQuantity = i.availableQuantity - :quantity,
            i.reservedQuantity = i.reservedQuantity + :quantity
        WHERE i.productId = :productId AND i.availableQuantity >= :quantity
    """)
    int reserveStock(@Param("productId") UUID productId, @Param("quantity") int quantity);

    // ATOMIC RELEASE
    @Modifying
    @Query("""
        UPDATE Inventory i
        SET i.availableQuantity = i.availableQuantity + :quantity,
            i.reservedQuantity = i.reservedQuantity - :quantity
        WHERE i.productId = :productId AND i.reservedQuantity >= :quantity
    """)
    int releaseStock(@Param("productId") UUID productId, @Param("quantity") int quantity);

    // ATOMIC COMMIT
    @Modifying
    @Query("""
        UPDATE Inventory i
        SET  i.reservedQuantity = i.reservedQuantity - :quantity
        WHERE i.productId = :productId AND i.reservedQuantity >= :quantity
    """)
    int commitStock(@Param("productId") UUID productId, @Param("quantity") int quantity);


}
