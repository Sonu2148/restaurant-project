//package com.restaurant.service;
//
//import com.restaurant.dto.OrderDto;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.UUID;
//
//public interface OrderService {
//    ResponseEntity<OrderDto> placeOrder(UUID userId);
//    ResponseEntity<List<OrderDto>> viewOrders(UUID userId);
//}
package com.restaurant.service;

import com.restaurant.dto.OrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    ResponseEntity<OrderDto> placeOrder(UUID userId);
    ResponseEntity<List<OrderDto>> viewOrders(UUID userId);
//    ResponseEntity<>
}