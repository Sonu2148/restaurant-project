package com.restaurant.service;

import com.restaurant.dto.UserDto;
import com.restaurant.dto.AddressDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserService {
    ResponseEntity<UserDto> registerUser(UserDto userDto);
    ResponseEntity<UserDto> addAddress(UUID userId, AddressDto addressDto);
    ResponseEntity<UserDto> editProfile(UUID userId, UserDto userDto);
    ResponseEntity<UserDto> getProfile(UUID userId);
    ResponseEntity<UserDto> updateUser(UUID userId, UserDto userDto);

}