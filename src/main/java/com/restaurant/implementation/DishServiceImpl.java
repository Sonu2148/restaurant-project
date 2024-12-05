package com.restaurant.implementation;

import com.restaurant.constants.Restaurant;
import com.restaurant.convert.DishConvert;
import com.restaurant.dto.DishDto;
import com.restaurant.entity.Dish;
import com.restaurant.enums.FoodCategory;
import com.restaurant.exception.ApiException;
import com.restaurant.repository.DishRepository;
import com.restaurant.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    private final DishConvert dishConvert;
    private final DishRepository dishRepository;

    public DishServiceImpl(DishConvert dishConvert, DishRepository dishRepository) {
        this.dishConvert = dishConvert;
        this.dishRepository = dishRepository;
    }

    @Override
    public ResponseEntity<DishDto> getDishById(UUID dishId) {
        try {
            Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new ApiException(Restaurant.DISH_NOT_FOUND));
            DishDto dishDto = dishConvert.convert(dish);

            return ResponseEntity.status(HttpStatus.OK).body(dishDto);
        } catch (Exception e) {
            DishDto error = new DishDto();
            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    public ResponseEntity<DishDto> addDishToMenu(DishDto dishDto) {
        try {
            if(ObjectUtils.isEmpty(dishDto)) {
                throw new ApiException(Restaurant.DISH_EMPTY);
            }

            Dish existingDish = dishRepository.findByName(dishDto.getName());

            Assert.isNull(existingDish, "Dish already exists");

            Dish dish = dishConvert.convert(dishDto);
            Dish savedDish = dishRepository.save(dish);
            DishDto savedDishDto = dishConvert.convert(savedDish);

            return ResponseEntity.status(HttpStatus.OK).body(savedDishDto);
        } catch (Exception e) {
            DishDto error = new DishDto();

            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @Override
    public ResponseEntity<List<DishDto>> getAllDishes() {
        try {
            List<Dish> allDishes = dishRepository.findAll();
            List<DishDto> allDishesDto = dishConvert.convert(allDishes);
            return ResponseEntity.status(HttpStatus.OK).body(allDishesDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<DishDto> udpateDish(DishDto dishDto) {
        try {
            Dish existingDish = dishRepository.findByName(dishDto.getName());

            if(ObjectUtils.isEmpty(existingDish) || existingDish.getId().equals(dishDto.getId())) {
                Dish dish = dishConvert.convert(dishDto);
                Dish savedDish = dishRepository.save(dish);
                DishDto savedDishDto = dishConvert.convert(savedDish);
                return ResponseEntity.status(HttpStatus.OK).body(savedDishDto);
            } else {
                throw new ApiException("Dish with same name already exist");
            }

        } catch (Exception e) {
            DishDto error = new DishDto();
            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    public ResponseEntity<String> removeDish(UUID dishId) {
        try {
            Dish existingDish = dishRepository.findById(dishId)
                    .orElseThrow(() -> new ApiException(Restaurant.DISH_NOT_FOUND));

            dishRepository.delete(existingDish);
            return ResponseEntity.status(HttpStatus.OK).body(Restaurant.DISH_REMOVED_SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Restaurant.DISH_REMOVED_FAILED);
        }
    }

    @Override
    public ResponseEntity<List<DishDto>> getAllAvailableDishes() {
        try {
            List<Dish> allDishes = dishRepository.findAllAvailableDishes();
            List<DishDto> allDishesDto = dishConvert.convert(allDishes);
            return ResponseEntity.status(HttpStatus.OK).body(allDishesDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<List<FoodCategory>> getCategories() {
        try {
            List<FoodCategory> allCategories = Arrays.asList(FoodCategory.class.getEnumConstants());
            return ResponseEntity.status(HttpStatus.OK).body(allCategories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<List<DishDto>> searchFood(String foodName) {
        try {
            List<Dish> relatedDishes = dishRepository.searchByFoodName(foodName);
            List<DishDto> relatedDishesDto = dishConvert.convert(relatedDishes);

            return ResponseEntity.status(HttpStatus.OK).body(relatedDishesDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<String> removeAvailability(UUID dishId) {
        try {
            Dish existingDish = dishRepository.findById(dishId).orElseThrow(() -> new ApiException(Restaurant.DISH_NOT_FOUND));
            if(existingDish.getIsAvailable()) {
                existingDish.setIsAvailable(false);
                dishRepository.save(existingDish);
                return ResponseEntity.status(HttpStatus.OK).body("Removed Successfully");
            } else {
                throw new ApiException("Already removed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @Override
//    public ResponseEntity<List<DishDto>> getAllDishes(FoodCategory foodCategory, String foodName, Boolean isVeg, boolean isAsc) {
//        List<Dish> allDishes;
//        if(!ObjectUtils.isEmpty(foodCategory)) {
//            if(!ObjectUtils.isEmpty(isVeg)) {
//                if(isAsc) {
//                    allDishes = dishRepository.getAllDishesByAsc(foodCategory.toString(), foodName, isVeg);
//                }
//                else {
//                    allDishes = dishRepository.getAllDishes(foodCategory.toString(), foodName, isVeg);
//                }
//            }else {
//                if(isAsc) {
//                    allDishes = dishRepository.getAllDishesByAsc(foodCategory.toString(), foodName);
//                }
//                else {
//                    allDishes = dishRepository.getAllDishes(foodCategory.toString(), foodName);
//                }
//            }
//
//        }
//        else {
//            if(!ObjectUtils.isEmpty(isVeg)) {
//                if(isAsc) {
//                    allDishes = dishRepository.getAllDishesByAsc(foodName, isVeg);
//                }
//                else {
//                    allDishes = dishRepository.getAllDishes(foodName, isVeg);
//                }
//            }
//            else {
//                if(isAsc) {
//                    allDishes = dishRepository.getAllDishesByAsc(foodName);
//                }
//                else {
//                    allDishes = dishRepository.getAllDishesByName(foodName);
//                }
//            }
//        }
//
//        List<DishDto> allDishesDto = dishConvert.convert(allDishes);
//        return ResponseEntity.status(HttpStatus.OK).body(allDishesDto);
//    }
}
