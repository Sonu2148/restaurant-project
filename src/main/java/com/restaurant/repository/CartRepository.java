package com.restaurant.repository;

import com.restaurant.entity.Cart;
import com.restaurant.entity.Dish;
import com.restaurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Cart findByUserAndFoodItems(User user, Dish dish);
    List<Cart> findByUser(User user);

}
