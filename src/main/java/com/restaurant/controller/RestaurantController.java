package com.restaurant.controller;

import com.restaurant.dto.DishDto;
import com.restaurant.implementation.DishServiceImpl;
import com.restaurant.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurant")
@CrossOrigin
public class RestaurantController {
    private final DishService dishService;

    @Autowired
    public RestaurantController(DishServiceImpl dishService) {
        this.dishService = dishService;
    }

    @PostMapping("/add-dish")
    public ResponseEntity<DishDto> addDish(@RequestBody DishDto dishDto) {
        return dishService.addDishToMenu(dishDto);
    }

    @PutMapping("/update-dish")
    public ResponseEntity<DishDto> updateDish(@RequestBody DishDto dishDto) {
        return dishService.udpateDish(dishDto);
    }

    @DeleteMapping("/remove-dish/{dishId}")
    public ResponseEntity removeDish(@PathVariable("dishId") UUID dishId) {
        return dishService.removeDish(dishId);
    }

    @GetMapping("/get-all-dishes")
    public ResponseEntity<List<DishDto>> getAllDishes() {
        return dishService.getAllDishes();
    }

    @PutMapping("/remove-availability/{dishId}")
    public ResponseEntity<String> removeAvailability(@PathVariable("dishId") UUID dishId) {
        return dishService.removeAvailability(dishId);
    }

}
