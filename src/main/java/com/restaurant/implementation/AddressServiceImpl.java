package com.restaurant.implementation;

import com.restaurant.constants.Restaurant;
import com.restaurant.convert.AddressConvert;
import com.restaurant.convert.UserConvert;
import com.restaurant.dto.AddressDto;
import com.restaurant.dto.UserDto;
import com.restaurant.entity.Address;
import com.restaurant.entity.User;
import com.restaurant.exception.ApiException;
import com.restaurant.repository.AddressRepository;
import com.restaurant.repository.UserRepository;
import com.restaurant.service.AddressService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userReposistory;
    private final AddressConvert addressConvert;
    private final UserConvert userConvert;
    private final EntityManager entityManager;

    @Autowired
    private AddressServiceImpl(AddressRepository addressRepository, EntityManager entityManager, UserConvert userConvert, UserRepository userReposistory, AddressConvert addressConvert) {
        this.addressRepository = addressRepository;
        this.entityManager = entityManager;
        this.userConvert = userConvert;
        this.addressConvert = addressConvert;
        this.userReposistory = userReposistory;
    }

    public ResponseEntity<UserDto> addAddress(UUID userId, AddressDto addressDto) {
        try {
            if(ObjectUtils.isEmpty(addressDto)) {
                throw new ApiException(Restaurant.ADDRESS_EMPTY);
            }

            User existingUser = userReposistory.findById(userId)
                    .orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));

            Address address = addressConvert.convert(addressDto);
            address.setUser(existingUser);

            if(existingUser.getAddressList().size() == 0) {
                address.setActive(Boolean.TRUE);
            }

            Address savedAddress = addressRepository.save(address);
            existingUser.getAddressList().add(address);
            userReposistory.save(existingUser);
            UserDto updatedUserDto = userConvert.convert(existingUser);

            return ResponseEntity.status(HttpStatus.OK).body(updatedUserDto);

        } catch (Exception e) {
            UserDto error = new UserDto();

            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    public ResponseEntity<UserDto> setActiveAddress(UUID userId, UUID addressId) {
        try {
            User existingUser = userReposistory.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));

            existingUser.getAddressList().forEach(address -> {
                if(address.getId().equals(addressId)) {
                    address.setActive(Boolean.TRUE);
                } else {
                    address.setActive(Boolean.FALSE);
                }
            });

            User updatedUser = userReposistory.save(existingUser);
            UserDto updatedUserDto = userConvert.convert(updatedUser);

            return ResponseEntity.status(HttpStatus.OK).body(updatedUserDto);
        } catch (Exception e) {
            UserDto error = new UserDto();

            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @Override
    public ResponseEntity<UserDto> removeAddress(UUID userId, UUID addressId) {
        try {
            Address address = addressRepository.findById(addressId).orElseThrow(() -> new ApiException(Restaurant.ADDRESS_NOT_FOUND));
            User existingUser = userReposistory.findById(userId).orElseThrow(() -> new ApiException(Restaurant.USER_NOT_FOUND));
            UserDto existingUserDto = userConvert.convert(existingUser);

            return ResponseEntity.status(HttpStatus.OK).body(existingUserDto);
        } catch (Exception e) {
            UserDto error = new UserDto();

            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
