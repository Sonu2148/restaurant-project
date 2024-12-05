package com.restaurant.implementation;

import com.restaurant.constants.Restaurant;
import com.restaurant.convert.CartConvert;
import com.restaurant.dto.CartDto;
import com.restaurant.dto.CartItemsDto;
import com.restaurant.entity.Cart;
import com.restaurant.entity.Dish;
import com.restaurant.entity.User;
import com.restaurant.exception.ApiException;
import com.restaurant.repository.CartRepository;
import com.restaurant.repository.DishRepository;
import com.restaurant.repository.UserRepository;
import com.restaurant.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartConvert cartConvert;

    public ResponseEntity<CartDto> addToCart(UUID userId, UUID dishId, Integer quantity) {
        try {
            User existingUser = userRepository.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
            Dish existingDish = dishRepository.findById(dishId).orElseThrow(() -> new ApiException(Restaurant.DISH_NOT_FOUND));

            Cart existingItem = cartRepository.findByUserAndFoodItems(existingUser, existingDish);
            if(!ObjectUtils.isEmpty(existingItem)) {
                throw new ApiException("Already in Cart");
            }

            if(quantity <= 0) {
                throw new ApiException("Quantity could not be negative");
            }

            Cart cart = new Cart();

            cart.setUser(existingUser);
            cart.setFoodItems(existingDish);
            cart.setQuantity(quantity);

            Cart savedCart = cartRepository.save(cart);
            CartDto savedCartDto = cartConvert.convert(savedCart);
            return ResponseEntity.status(HttpStatus.OK).body(savedCartDto);
        } catch (ApiException e) {
            CartDto error = new CartDto();
            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        catch (Exception e) {
            CartDto error = new CartDto();
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @Override
    public ResponseEntity<CartDto> updateCart(UUID userId, UUID dishId, Integer quantity) {
        try {
            User existingUser = userRepository.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
            Dish existingDish = dishRepository.findById(dishId).orElseThrow(() -> new ApiException(Restaurant.DISH_NOT_FOUND));

            Cart existingItem = cartRepository.findByUserAndFoodItems(existingUser, existingDish);

            if(quantity <= 0) {
                throw new ApiException("Quantity could not be negative");
            }

            existingItem.setQuantity(quantity);

            Cart savedCart = cartRepository.save(existingItem);
            CartDto cartDto = cartConvert.convert(savedCart);

            return ResponseEntity.status(HttpStatus.OK).body(cartDto);
        } catch (Exception e) {
            CartDto error = new CartDto();
            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @Override
    public ResponseEntity<List<CartItemsDto>> viewCart(UUID userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));

            List<Cart> cartItems = cartRepository.findByUser(user);

            List<CartItemsDto> cartItemsDtos = cartConvert.convertToCartItems(cartItems);
            return ResponseEntity.status(HttpStatus.OK).body(cartItemsDtos);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<String> removeFromCart(UUID userId, UUID dishId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
            Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new ApiException(Restaurant.DISH_NOT_FOUND));
            Cart existingItem = cartRepository.findByUserAndFoodItems(user, dish);
            if(ObjectUtils.isEmpty(existingItem)) {
                throw new ApiException("Item not present in the cart");
            }
            cartRepository.delete(existingItem);
            return ResponseEntity.status(HttpStatus.OK).body("Removed Successfully");
        } catch(ApiException api) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item not present in the cart");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("some thing went wrong");
        }
    }

    @Override
    public ResponseEntity<String> addQuantity(UUID userId, UUID dishId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
            Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new ApiException(Restaurant.DISH_NOT_FOUND));
            Cart existingItem = cartRepository.findByUserAndFoodItems(user, dish);
            if(ObjectUtils.isEmpty(existingItem)) {
                throw new ApiException("Item not present in the cart");
            }
            existingItem.setQuantity(existingItem.getQuantity() +1);
            Cart updatedCart = cartRepository.save(existingItem);
            return ResponseEntity.status(HttpStatus.OK).body("quantity added");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("something went wrong");
        }
    }

    public ResponseEntity<String> removeQuantity(UUID userId, UUID dishId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
            Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new ApiException(Restaurant.DISH_NOT_FOUND));
            Cart existingItem = cartRepository.findByUserAndFoodItems(user, dish);
            if(ObjectUtils.isEmpty(existingItem)) {
                throw new ApiException("Item not present in the cart");
            }
            if(existingItem.getQuantity() != 1) {
                existingItem.setQuantity(existingItem.getQuantity() -1);
                Cart updatedCart = cartRepository.save(existingItem);
                return ResponseEntity.status(HttpStatus.OK).body("quantity removed");
            }
            return ResponseEntity.status(HttpStatus.OK).body("quantity is 1, try to remove it");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("something went wrong");
        }
    }
}