package com.restaurant.convert;

import com.restaurant.dto.CartDto;
import com.restaurant.dto.CartItemsDto;
import com.restaurant.dto.DishDto;
import com.restaurant.dto.UserDto;
import com.restaurant.entity.Cart;
import com.restaurant.entity.Dish;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CartConvert {
    public CartDto convert(Cart cart) {
        CartDto cartDto = new CartDto();

        if(!ObjectUtils.isEmpty(cart)) {
            BeanUtils.copyProperties(cart, cartDto);
            if(!ObjectUtils.isEmpty(cart.getFoodItems())) {
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(cart.getFoodItems(),dishDto);
                cartDto.setFoodItems(dishDto);
            }
            if(!ObjectUtils.isEmpty(cart.getUser())) {
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(cart.getUser(), userDto);
                cartDto.setUser(userDto);
            }
        }
        return cartDto;
    }

    public List<CartItemsDto> convertToCartItems(List<Cart> cartList) {
        List<CartItemsDto> cartItemsDtos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(cartList)) {
            cartList.forEach(cart -> {
                CartItemsDto cartItem = new CartItemsDto();
                BeanUtils.copyProperties(cart, cartItem);
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(cart.getFoodItems(),dishDto);
                cartItem.setFoodItems(dishDto);
                cartItem.setQuantity(cart.getQuantity());
                cartItemsDtos.add(cartItem);
            });
        }

        return cartItemsDtos;

    }
}
