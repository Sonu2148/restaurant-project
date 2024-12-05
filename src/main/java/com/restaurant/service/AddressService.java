package com.restaurant.service;

import com.restaurant.dto.AddressDto;
import com.restaurant.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AddressService {
    ResponseEntity<UserDto> addAddress(UUID userId, AddressDto addressDto);
    ResponseEntity<UserDto> setActiveAddress(UUID userId, UUID addressId);
    ResponseEntity<UserDto> removeAddress(UUID userId, UUID addressId);
}
