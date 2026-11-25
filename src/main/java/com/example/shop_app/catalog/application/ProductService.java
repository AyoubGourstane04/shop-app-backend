package com.example.shop_app.catalog.application;

import com.example.shop_app.catalog.api.dto.CreateProductRequest;
import com.example.shop_app.catalog.api.dto.ProductResponse;
import com.example.shop_app.catalog.domain.Product;
import com.example.shop_app.catalog.domain.ProductStatus;
import com.example.shop_app.catalog.infrastructure.ProductRepository;
import com.example.shop_app.catalog.infrastructure.ProductSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
                request.currency(),
                request.imageUrl(),
                request.categoryId(),
                request.isFeatured() != null && request.isFeatured()
        );

        Product saved = productRepository.save(product);
        return ProductResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(
            String query,
            String categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean featured,
            Pageable pageable
    ){
        Specification<Product> spec = ProductSpecification.filterBy(
                query,
                categoryId,
                minPrice,
                maxPrice,
                featured,
                ProductStatus.PUBLISHED
        );

        return productRepository.findAll(spec, pageable)
                .map(ProductResponse::fromEntity);
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
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(UUID id){
        log.info("Evicting product id: {} from cache and database...", id);
        productRepository.deleteById(id);
    }


}
