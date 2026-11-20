package com.example.shop_app.order.application;

import com.example.shop_app.Inventory.application.InventoryService;
import com.example.shop_app.catalog.api.dto.ProductResponse;
import com.example.shop_app.catalog.application.ProductService;
import com.example.shop_app.catalog.domain.Product;
import com.example.shop_app.common.service.IdempotencyService;
import com.example.shop_app.order.api.dto.CheckoutRequest;
import com.example.shop_app.order.api.dto.OrderItemRequest;
import com.example.shop_app.order.api.dto.OrderResponse;
import com.example.shop_app.order.domain.Order;
import com.example.shop_app.order.domain.OrderItem;
import com.example.shop_app.order.infrastructure.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final IdempotencyService idempotencyService;

    @Transactional
    public OrderResponse processCheckout(String idempotencyKey, CheckoutRequest request){
        Optional<OrderResponse> cachedResponse = idempotencyService.getCachedResponse(idempotencyKey, OrderResponse.class);
        if(cachedResponse.isPresent()){
            log.info("Returning cached order response for Idempotency-Key: {}", idempotencyKey);
            return cachedResponse.get();
        }

        if(!idempotencyService.lock(idempotencyKey)){
            throw new IllegalStateException("Duplicate request in progress for key: " + idempotencyKey);
        }

        try {
            Order order = new Order(request.customerId(), request.currency());
            for(OrderItemRequest req : request.items()){
                ProductResponse product = productService.getProductById(req.productId());

                inventoryService.reserveStock(product.id(), req.quantity());

                OrderItem item = new OrderItem(product.id(), product.name(), product.price(), req.quantity());
                order.addItem(item);
            }

            Order saved = orderRepository.save(order);

            OrderResponse response = OrderResponse.fromEntity(saved);

            idempotencyService.complete(idempotencyKey, response);

            return response;

        } catch (Exception e) {
            idempotencyService.unlock(idempotencyKey);
            throw e;
        }


    }








}
