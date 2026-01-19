package com.mehulagawal.e_commerce.controller;

import com.mehulagawal.e_commerce.model.CART_ITEM;
import com.mehulagawal.e_commerce.model.Order;
import com.mehulagawal.e_commerce.model.Product;
import com.mehulagawal.e_commerce.repository.CartRepository;
import com.mehulagawal.e_commerce.repository.OrderRepository;
import com.mehulagawal.e_commerce.repository.ProductRepository;
import com.mehulagawal.e_commerce.util.OrderItem;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(
            @RequestHeader("userId") String userId) {

        List<CART_ITEM> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(null);
        }

        List<String> productIds = cartItems.stream()
                .map(CART_ITEM::getProductId)
                .toList();

        Map<String, Product> productMap =
                productRepository.findAllById(productIds).stream()
                        .collect(Collectors.toMap(Product::getId, p -> p));

        List<OrderItem> orderItems = cartItems.stream()
                .map(ci -> {
                    Product p = productMap.get(ci.getProductId());
                    if (p == null) {
                        throw new RuntimeException("Product not found");
                    }
                    return new OrderItem(
                            ci.getProductId(),
                            ci.getQuantity(),
                            p.getPrice()
                    );
                }).toList();

        double totalAmount = orderItems.stream()
                .mapToDouble(i -> i.getQuantity() * i.getPrice())
                .sum();

        Order order = new Order();
        order.setUserId(userId);
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setStatus("CREATED");
        order.setCreatedAt(new Date());
        ;

        cartRepository.deleteByUserId(userId);

        return ResponseEntity.ok(orderRepository.save(order));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> AllUserOrder(@RequestHeader("userId") String userId) {
        return ResponseEntity.ok(orderRepository.findByUserId(userId));
    }
}
