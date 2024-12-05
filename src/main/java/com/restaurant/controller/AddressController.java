package com.restaurant.controller;

import com.restaurant.dto.AddressDto;
import com.restaurant.dto.UserDto;
import com.restaurant.implementation.AddressServiceImpl;
import com.restaurant.service.AddressService;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/customer/address")
@CrossOrigin
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressServiceImpl addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/add-address/{userId}")
    public ResponseEntity<UserDto> addAddress(@PathVariable("userId") UUID userId, @RequestBody AddressDto addressDto) {
        return addressService.addAddress(userId, addressDto);
    }

    @PutMapping("/set-active/{userId}/{addressId}")
    public ResponseEntity<UserDto> setActiveAddress(@PathVariable("userId") UUID userId, @PathVariable("addressId") UUID addressId) {
        return addressService.setActiveAddress(userId, addressId);
    }

    @DeleteMapping("/remove-address/{userId}/{addressId}")
    public void removeAddress(@PathVariable("userId") UUID userId, @PathVariable("addressId") UUID addressId) {
        addressService.removeAddress(userId, addressId);
    }
}
