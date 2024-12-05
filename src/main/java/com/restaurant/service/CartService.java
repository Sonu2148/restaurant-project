package com.restaurant.service;

import com.restaurant.dto.CartDto;
import com.restaurant.dto.CartItemsDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {
    ResponseEntity<CartDto> addToCart(UUID userId, UUID dishId, Integer quantity);
    ResponseEntity<CartDto> updateCart(UUID userId, UUID dishId, Integer quantity);
    ResponseEntity<List<CartItemsDto>> viewCart(UUID userId);
    ResponseEntity<String> removeFromCart(UUID userId, UUID dishId);
    ResponseEntity<String> addQuantity(UUID userId, UUID dishId);
    ResponseEntity<String> removeQuantity(UUID userId, UUID dishId);
}