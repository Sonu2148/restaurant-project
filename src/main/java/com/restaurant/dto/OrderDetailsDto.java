package com.restaurant.dto;

import com.restaurant.entity.Dish;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDto extends BaseResponse {
    private UUID id;
    private Dish foodItem;
    private Double costPerItem;
    private Double totalPrice;
    private Integer quantity;
}