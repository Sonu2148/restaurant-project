package com.restaurant.service;

import com.restaurant.dto.LoginDto;
import com.restaurant.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<UserDto> validateLogin(LoginDto loginDto);
}
