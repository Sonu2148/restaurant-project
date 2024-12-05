package com.restaurant.controller;

import com.restaurant.dto.OrderDto;
import com.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/customer/orders")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/place-order/{userId}")
    public ResponseEntity<OrderDto> placeOrder(@PathVariable("userId") UUID userId) {
        return orderService.placeOrder(userId);
    }


    @GetMapping("/view-orders/{userId}")
    public ResponseEntity<List<OrderDto>> viewOrders(@PathVariable("userId") UUID userId) {
        return orderService.viewOrders(userId);
    }
}

