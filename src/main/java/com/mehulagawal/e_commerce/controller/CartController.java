package com.mehulagawal.e_commerce.controller;


import com.mehulagawal.e_commerce.model.CART_ITEM;
import com.mehulagawal.e_commerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    CartRepository cartRepository;

    @PostMapping("/cart/add")
    public ResponseEntity<CART_ITEM> addToCart(@RequestBody CART_ITEM cartItem) {
        return ResponseEntity.ok(cartRepository.save(cartItem));
    }

    @GetMapping("/cart")
    public ResponseEntity<List<CART_ITEM>> getUserCart(@RequestHeader("userId") String userId) {

        List<CART_ITEM> items = cartRepository.findByUserId(userId);

        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/cart/clear")
    public ResponseEntity<Map<String, String>> clearUserCart(@RequestHeader("userId") String userId) {
        cartRepository.deleteByUserId(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");

        return ResponseEntity.ok(response);
    }

}
