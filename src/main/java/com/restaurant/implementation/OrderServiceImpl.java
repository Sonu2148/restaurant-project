package com.restaurant.implementation;

import com.restaurant.constants.Restaurant;
import com.restaurant.convert.OrderConvert;
import com.restaurant.dto.OrderDto;
import com.restaurant.entity.Address;
import com.restaurant.entity.Cart;
import com.restaurant.entity.OrderDetails;
import com.restaurant.entity.Orders;
import com.restaurant.entity.User;
import com.restaurant.exception.ApiException;
import com.restaurant.repository.*;
import com.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderConvert orderConvert;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public ResponseEntity<OrderDto> placeOrder(UUID userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
            Address activeAddress = addressRepository.findByUserAndIsActiveTrue(user)
                    .orElseThrow(() -> new ApiException("Active address not found for user"));
            List<Cart> cartItems = cartRepository.findByUser(user);
            if(CollectionUtils.isEmpty(cartItems)) {
                throw new ApiException("Can't place order, No items in cart");
            }
            Orders orders = new Orders();
            double totalCost = 0;
            orders.setUser(user);
            orders.setAddress(activeAddress);
            for (Cart item : cartItems) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setQuantity(item.getQuantity());
                orderDetails.setCostPerItem(item.getFoodItems().getPrice());
                orderDetails.setTotalPrice(item.getQuantity() * item.getFoodItems().getPrice());
                orderDetails.setFoodItem(item.getFoodItems());
                totalCost += item.getQuantity()*item.getFoodItems().getPrice();
                OrderDetails savedOrderDetail = orderDetailsRepository.save(orderDetails);
                orders.getOrderItems().add(savedOrderDetail);
            }
            orders.setTotalBill(totalCost);
            cartRepository.deleteAll(cartItems);
            Orders savedOrder = orderRepository.save(orders);
            OrderDto savedOrderDto = orderConvert.convert(orders);
            return ResponseEntity.status(HttpStatus.OK).body(savedOrderDto);
        } catch (ApiException api) {
            OrderDto error = new OrderDto();
            error.setStatus(HttpStatus.OK);
            error.setMessage(api.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(error);
        } catch (Exception e) {
            OrderDto error = new OrderDto();
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Override
    public ResponseEntity<List<OrderDto>> viewOrders(UUID userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
            List<Orders> ordersList = orderRepository.findByUser(user);
            ordersList.sort((o1, o2) -> o2.getOrderedDateTime().compareTo(o1.getOrderedDateTime()));
            List<OrderDto> orderDtoList = orderConvert.convert(ordersList);
            return ResponseEntity.status(HttpStatus.OK).body(orderDtoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


//    @Override
//    public ResponseEntity<List<OrderDto>> viewOrders(UUID userId) {
//        try {
//            User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
//            List<Orders> ordersList = orderRepository.findByUser(user);
//
//            Address activeAddress = user.getAddressList().stream().filter(Address::isActive).findAny().get();
//
//            if(ObjectUtils.isEmpty(activeAddress)) {
//                throw new ApiException("Can't place orders, you have to add or select Address");
//            }
//
//            List<OrderDto> orderDtoList = orderConvert.convert(ordersList);
//            return ResponseEntity.status(HttpStatus.OK).body(orderDtoList);
//        } catch(Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
}
//package com.restaurant.implementation;
//
//import com.restaurant.constants.Restaurant;
//import com.restaurant.convert.OrderConvert;
//import com.restaurant.dto.OrderDto;
//import com.restaurant.entity.Address;
//import com.restaurant.entity.Cart;
//import com.restaurant.entity.OrderDetails;
//import com.restaurant.entity.Orders;
//import com.restaurant.entity.User;
//import com.restaurant.exception.ApiException;
//import com.restaurant.repository.*;
//import com.restaurant.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ObjectUtils;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class OrderServiceImpl implements OrderService {
//    @Autowired
//    private CartRepository cartRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private OrderRepository orderRepository;
//    @Autowired
//    private OrderDetailsRepository orderDetailsRepository;
//    @Autowired
//    private OrderConvert orderConvert;
//    @Autowired
//    private AddressRepository addressRepository;
//
//
//    @Override
//    public ResponseEntity<OrderDto> placeOrder(UUID userId) {
//        try {
//            User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
//
//            List<Cart> cartItems = cartRepository.findByUser(user);
//            if (cartItems.isEmpty()) {
//                throw new ApiException("Cart is empty, cannot place order.");
//            }
//
//            Address activeAddress = addressRepository.findByUserAndIsActiveTrue(user)
//                    .orElseThrow(() -> new ApiException("Active address not found for user."));
//
//            Orders orders = new Orders();
//            orders.setUser(user);
//            orders.setAddress(activeAddress);
//
//            cartItems.forEach(item -> {
//                OrderDetails orderDetails = new OrderDetails();
//                orderDetails.setQuantity(item.getQuantity());
//                orderDetails.setFoodItem(item.getFoodItems());
//                OrderDetails savedOrderDetail = orderDetailsRepository.save(orderDetails);
//                orders.getOrderItems().add(savedOrderDetail);
//            });
//
//            cartRepository.deleteAll(cartItems);
//
//            Orders savedOrder = orderRepository.save(orders);
//            OrderDto savedOrderDto = orderConvert.convert(savedOrder); // Convert with address details
//            return ResponseEntity.status(HttpStatus.OK).body(savedOrderDto);
//        } catch (Exception e) {
//            OrderDto error = new OrderDto();
//            error.setStatus(HttpStatus.BAD_REQUEST);
//            error.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//        }
//    }
//
////    public ResponseEntity<OrderDto> placeOrder(UUID userId) {
////        try {
////            User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
////
////            List<Cart> cartItems = cartRepository.findByUser(user);
////
////            Orders orders = new Orders();
////
////            orders.setUser(user);
////            cartItems.forEach(item -> {
////                OrderDetails orderDetails = new OrderDetails();
////                orderDetails.setQuantity(item.getQuantity());
////                orderDetails.setFoodItem(item.getFoodItems());
////                OrderDetails savedOrderDetail = orderDetailsRepository.save(orderDetails);
////                orders.getOrderItems().add(savedOrderDetail);
////            });
////
////            cartRepository.deleteAll(cartItems);
////
////            Orders savedOrder = orderRepository.save(orders);
////            OrderDto savedOrderDto = orderConvert.convert(orders);
////            return ResponseEntity.status(HttpStatus.OK).body(savedOrderDto);
////        } catch (Exception e) {
////            OrderDto error = new OrderDto();
////            error.setStatus(HttpStatus.BAD_REQUEST);
////            error.setMessage(e.getMessage());
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
////        }
////    }
//
//    @Override
//    public ResponseEntity<List<OrderDto>> viewOrders(UUID userId) {
//        try {
//            User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
//            List<Orders> ordersList = orderRepository.findByUser(user);
//
//            Address activeAddress = user.getAddressList().stream().filter(Address::isActive).findAny().get();
//
//            if(ObjectUtils.isEmpty(activeAddress)) {
//                throw new ApiException("Can't place orders, you have to add or select Address");
//            }
//
//            List<OrderDto> orderDtoList = orderConvert.convert(ordersList);
//            return ResponseEntity.status(HttpStatus.OK).body(orderDtoList);
//        } catch(Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
//}
