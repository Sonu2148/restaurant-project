package com.restaurant.convert;

import com.restaurant.dto.AddressDto;
import com.restaurant.dto.OrderDetailsDto;
import com.restaurant.dto.OrderDto;
import com.restaurant.dto.UserDto;
import com.restaurant.entity.Orders;
import jakarta.persistence.criteria.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderConvert {

    public OrderDto convert(Orders orders) {
        OrderDto orderDto = new OrderDto();

        if (!ObjectUtils.isEmpty(orders)) {
            BeanUtils.copyProperties(orders, orderDto);

            // Map User
            if (!ObjectUtils.isEmpty(orders.getUser())) {
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(orders.getUser(), userDto);
                orderDto.setUser(userDto);
            }

            // Map Order Items
            if (!CollectionUtils.isEmpty(orders.getOrderItems())) {
                orders.getOrderItems().forEach(orderItem -> {
                    OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
                    BeanUtils.copyProperties(orderItem, orderDetailsDto);

                    // Include Food Item Details
                    if (orderItem.getFoodItem() != null) {
                        orderDetailsDto.setFoodItem(orderItem.getFoodItem());
                    }

                    orderDto.getOrderItems().add(orderDetailsDto);
                });
            }

            // Map Address
            if (!ObjectUtils.isEmpty(orders.getAddress())) {
                AddressDto addressDto = new AddressDto();
                BeanUtils.copyProperties(orders.getAddress(), addressDto);
                orderDto.setAddress(addressDto);
            }

            // Explicitly set the Total Bill
            orderDto.setBill(orders.getTotalBill());
        }

        return orderDto;
    }

    public List<OrderDto> convert(List<Orders> ordersList) {
        List<OrderDto> orderDtoList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(ordersList)) {
            ordersList.forEach(order -> {
                OrderDto orderDto = convert(order);
                orderDtoList.add(orderDto);
            });
        }

        return orderDtoList;
    }



//    public List<OrderDto> convert(List<Orders> ordersList) {
//        List<OrderDto> orderDtoList = new ArrayList<>();
//
//        if(!CollectionUtils.isEmpty(ordersList)) {
//            ordersList.forEach(orderlist -> {
//                OrderDto order = convert(orderlist);
//                orderDtoList.add(order);
//            });
//        }
//
//        return orderDtoList;
//    }
}
