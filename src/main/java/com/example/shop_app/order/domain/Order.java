package com.example.shop_app.order.domain;

import com.example.shop_app.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Column(name = "order_number", nullable = false, unique = true, length = 64)
    private String orderNumber;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "status", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 3)
    private BigDecimal totalAmount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "MAD";

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order(UUID customerId, String currency){
        this.orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        this.customerId = customerId;
        this.status = OrderStatus.PENDING;
        this.currency = currency != null ? currency : "MAD";
        this.totalAmount = BigDecimal.ZERO;
    }

    public void addItem(OrderItem item){
        items.add(item);
        item.setOrder(this);
        this.totalAmount = this.totalAmount.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
    }



}
