package com.restaurant.controller;

import com.restaurant.dto.DishDto;
import com.restaurant.enums.FoodCategory;
import com.restaurant.implementation.DishServiceImpl;
import com.restaurant.service.DishService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer/dish")
@CrossOrigin
public class DishController {
    private final DishService dishService;
    @Autowired
    public DishController(DishServiceImpl dishService) {
        this.dishService = dishService;
    }

//    @GetMapping("/filter-dishes")
//    public ResponseEntity<List<DishDto>> getAllDishes(@Nullable @RequestParam("foodCategory") FoodCategory foodCategory,@Nullable @RequestParam("isVeg") Boolean isVeg,@RequestParam("foodName") String foodName, @RequestParam("isAsc") boolean isAsc) {
//        return dishService.getAllDishes(foodCategory, foodName, isVeg, isAsc);
//    }

    @GetMapping("/search/{foodName}")
    public ResponseEntity<List<DishDto>> searchDishes(@PathVariable("foodName") String foodName) {
        return dishService.searchFood(foodName);
    }

    @GetMapping("/get-dish-by-id/{dishId}")
    public ResponseEntity<DishDto> getDishById(@PathVariable("dishId")UUID dishId) {
        return dishService.getDishById(dishId);
    }

    @GetMapping("/get-all-dishes")
    public ResponseEntity<List<DishDto>> getAllDishes() {
        return dishService.getAllAvailableDishes();
    }

    @GetMapping("/get-categories")
    public ResponseEntity<List<FoodCategory>> getCategories() {
        return dishService.getCategories();
    }
}
