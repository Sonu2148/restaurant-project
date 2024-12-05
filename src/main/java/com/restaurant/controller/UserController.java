package com.restaurant.controller;

import com.restaurant.dto.UserDto;
import com.restaurant.dto.AddressDto;
import com.restaurant.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customer/users")
@CrossOrigin

@Tag(name = "User Controller", description = "Operations related to user management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register User", description = "Registers a new user and returns the created user details.")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @PatchMapping("/{userId}/update")
    @Operation(summary = "Update User", description = "Updates specific fields of the user profile.")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID userId, @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }
    @PostMapping("/{userId}/add-address")
    @Operation(summary = "Add Address", description = "Adds a new address to an existing user by user ID.")
    public ResponseEntity<UserDto> addAddress(@PathVariable UUID userId, @RequestBody AddressDto addressDto) {
        return userService.addAddress(userId, addressDto);
    }

    @PutMapping("/{userId}/edit")
    @Operation(summary = "Edit Profile", description = "Updates user details, including addresses, by user ID.")
    public ResponseEntity<UserDto> editProfile(@PathVariable UUID userId, @RequestBody UserDto userDto) {
        return userService.editProfile(userId, userDto);
    }

    @GetMapping("/{userId}/profile")
    @Operation(summary = "Get Profile", description = "Fetches the profile of a user by user ID.")
    public ResponseEntity<UserDto> getProfile(@PathVariable UUID userId) {
        return userService.getProfile(userId);
    }
}