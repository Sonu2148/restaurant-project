package com.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto extends BaseResponse {
    private UUID id;
    private String houseNo;
    private boolean isActive;
    private String area;
    private String city;
    private String state;
    private String pincode;
}
