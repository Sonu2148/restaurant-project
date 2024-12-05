package com.restaurant.repository;

import com.restaurant.entity.Dish;
import com.restaurant.enums.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {

    Dish findByName(String dishName);

    @Query(nativeQuery = true, value = "select * from food_items where name like %:foodName% and is_available = true")
    List<Dish> searchByFoodName(@Param("foodName") String foodName);

//    @Query(nativeQuery = true, value = "select * from food_items where name like %:foodName%")
//    List<Dish> getAllAvailableFoodItems(@Param("foodName") String foodName);

    @Query(nativeQuery = true, value="select * from food_items where is_available = true")
    List<Dish> findAllAvailableDishes();
}