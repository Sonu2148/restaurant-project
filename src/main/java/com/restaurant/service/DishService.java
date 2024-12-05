package com.restaurant.service;

import com.restaurant.dto.DishDto;
import com.restaurant.enums.FoodCategory;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface DishService {
    ResponseEntity<DishDto> addDishToMenu(DishDto dishDto);
    ResponseEntity<DishDto> getDishById(UUID dishId);
    ResponseEntity<List<DishDto>> getAllDishes();
    ResponseEntity<DishDto> udpateDish(DishDto dishDto);
    ResponseEntity<String> removeDish(UUID dishId);
    ResponseEntity<List<DishDto>> getAllAvailableDishes();
    ResponseEntity<List<FoodCategory>> getCategories();
    ResponseEntity<List<DishDto>> searchFood(String foodName);
    ResponseEntity<String> removeAvailability(UUID dishId);
}
