package com.restaurant.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends BaseResponse {
    private UUID id;
    private UserDto user;
    private List<OrderDetailsDto> orderItems = new ArrayList<>();
    private Double bill;
    private LocalDateTime orderedDateTime;
    private AddressDto address;
}