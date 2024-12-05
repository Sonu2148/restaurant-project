package com.restaurant.convert;

import com.restaurant.dto.DishDto;
import com.restaurant.entity.Dish;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class DishConvert {
    public Dish convert(DishDto dishDto) {
        Dish dish = new Dish();

        if(!ObjectUtils.isEmpty(dishDto)) {
            BeanUtils.copyProperties(dishDto, dish);
        }
        return dish;
    }

    public DishDto convert(Dish dish) {
        DishDto dishDto = new DishDto();

        if(!ObjectUtils.isEmpty(dish)) {
            BeanUtils.copyProperties(dish, dishDto);
        }

        return dishDto;
    }

    public List<DishDto> convert(List<Dish> dishList) {
        List<DishDto> dishDtoList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(dishList)) {
            dishList.forEach(dish -> {
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(dish, dishDto);
                dishDtoList.add(dishDto);
            });
        }

        return dishDtoList;
    }
}
