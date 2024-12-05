package com.restaurant.controller;

import com.restaurant.dto.CartDto;
import com.restaurant.dto.CartItemsDto;
import com.restaurant.implementation.CartServiceImpl;
import com.restaurant.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/customer/cart")
@CrossOrigin
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/view-cart/{userId}")
    public ResponseEntity<List<CartItemsDto>> viewCart(@PathVariable("userId") UUID userId) {
        return cartService.viewCart(userId);
    }

    @PostMapping("/add-to-cart/{userId}/{dishId}/{quantity}")
    public ResponseEntity<CartDto> addToCart(@PathVariable("userId")UUID userId, @PathVariable("dishId") UUID dishId, @PathVariable("quantity") Integer quantity) {
        return cartService.addToCart(userId, dishId, quantity);
    }

    @PutMapping("/add-quantity/{userId}/{dishId}")
    public ResponseEntity<String> addQuantity(@PathVariable("userId") UUID userId, @PathVariable("dishId") UUID dishId) {
        return cartService.addQuantity(userId, dishId);
    }

    @PutMapping("/remove-quantity/{userId}/{dishId}")
    public ResponseEntity<String> removeQuantity(@PathVariable("userId") UUID userId, @PathVariable("dishId") UUID dishId) {
        return cartService.removeQuantity(userId, dishId);
    }

    @DeleteMapping("/remove-elements/{userId}/{dishId}")
    public ResponseEntity<String> removeFromCart(@PathVariable("userId") UUID userId, @PathVariable("dishId") UUID dishId) {
        return cartService.removeFromCart(userId, dishId);
    }

    @PatchMapping("/update-quantity/{userId}/{dishId}/{quantity}")
    public ResponseEntity<CartDto> updateCart(@PathVariable("userId") UUID userId, @PathVariable("dishId") UUID dishId, @PathVariable("quantity") Integer quantity) {
        return cartService.updateCart(userId, dishId, quantity);
    }
}