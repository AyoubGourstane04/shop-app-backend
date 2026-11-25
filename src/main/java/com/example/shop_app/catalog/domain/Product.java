package com.example.shop_app.catalog.domain;

import com.example.shop_app.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Getter
@Setter
public class Product extends BaseEntity {

    @Column(nullable = false, unique = true, length = 64)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 3)
    private String currency = "MAD";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ProductStatus status = ProductStatus.DRAFT;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "category_id", length = 64)
    private String categoryId;

    @Column(name = "is_featured", nullable = false)
    private boolean isFeatured = false;



    public Product(String sku, String name, String description, BigDecimal price, String currency,  String imageUrl, String categoryId, boolean isFeatured) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = (currency != null) ? currency : "MAD";
        this.status = ProductStatus.DRAFT;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.isFeatured = isFeatured;
    }

    public Product(String sku, String name, String description, BigDecimal price, String currency) {
        this(sku, name, description, price, currency, null, null, false);
    }

}
