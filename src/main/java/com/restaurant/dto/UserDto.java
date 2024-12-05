package com.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.restaurant.entity.Address;
import com.restaurant.enums.UserType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseResponse {
    private UUID id;
    private String name;
    private String mobileNo;
    private String email;
    private String role;
    private String password;
    private List<AddressDto> addressList = new ArrayList<>();
}

//{
//        "status": null,
//        "message": null,
//        "id": "7c7d95f1-c1fd-4533-b13f-6c4f9dc39c4d",
//        "name": "john",
//        "mobileNo": "9876543210",
//        "email": "john@gmail.com",
//        "role": "CUSTOMER",
//        "password": "1234",
//        "orders": null,
//        "addressList": []
//        }