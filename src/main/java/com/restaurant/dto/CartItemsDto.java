package com.restaurant.dto;

import com.restaurant.entity.Dish;
import com.restaurant.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class CartItemsDto {
    private UUID id;
    private DishDto foodItems;
    private Integer quantity;
}
