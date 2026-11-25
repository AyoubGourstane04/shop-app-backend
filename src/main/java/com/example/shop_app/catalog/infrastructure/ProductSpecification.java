package com.example.shop_app.catalog.infrastructure;

import com.example.shop_app.catalog.domain.Product;
import com.example.shop_app.catalog.domain.ProductStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductSpecification {
    public static Specification<Product> filterBy(
            String query,
            String categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean featured,
            ProductStatus status
    ){
        return ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(status != null){
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if(query != null && !query.trim().isEmpty()){
                String pattern = "%" + query.trim().toLowerCase() + "%";
                Predicate nameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern);
                Predicate descMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern);
                predicates.add(criteriaBuilder.or(nameMatch, descMatch));
            }

            if(categoryId != null && !categoryId.trim().isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), categoryId.trim()));
            }

            if(minPrice != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if(maxPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if(featured != null){
                predicates.add(criteriaBuilder.equal(root.get("isFeatured"), featured));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
