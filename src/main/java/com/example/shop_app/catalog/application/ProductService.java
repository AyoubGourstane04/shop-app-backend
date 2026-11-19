package com.example.shop_app.catalog.application;

import com.example.shop_app.catalog.api.dto.CreateProductRequest;
import com.example.shop_app.catalog.api.dto.ProductResponse;
import com.example.shop_app.catalog.domain.Product;
import com.example.shop_app.catalog.infrastructure.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request){
        if(productRepository.existsBySku(request.sku())){
            throw new IllegalArgumentException("Product with SKU " + request.sku() + " already exists");
        }

        Product product = new Product(
                request.sku(),
                request.name(),
                request.description(),
                request.price(),
                request.currency()
        );

        Product saved = productRepository.save(product);
        return ProductResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(UUID id){
        log.info("Cache miss for product id: {}. Fetching from Database...", id);

        return productRepository.findById(id)
                .map(ProductResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Transactional
    @Cacheable(value = "products", key = "#id")
    public void deleteProduct(UUID id){
        log.info("Evicting product id: {} from cache and database...", id);
        productRepository.deleteById(id);
    }


}
