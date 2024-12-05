package com.restaurant.implementation;


import com.restaurant.constants.Restaurant;
import com.restaurant.dto.UserDto;
import com.restaurant.dto.AddressDto;
import com.restaurant.entity.User;
import com.restaurant.entity.Address;
import com.restaurant.enums.UserType;
import com.restaurant.exception.ApiException;

import com.restaurant.repository.UserRepository;
import com.restaurant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import com.restaurant.convert.UserConvert;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConvert userConvert;
//    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public UserServiceImpl(UserRepository userRepository, UserConvert userConvert) {
        this.userRepository = userRepository;
        this.userConvert = userConvert;
    }

    @Override
    public ResponseEntity<UserDto> registerUser(UserDto userDto) {
        try {
            if (ObjectUtils.isEmpty(userDto)) {
                throw new ApiException("User details cannot be empty.");
            }

            User user = userConvert.convert(userDto);
            User existingUser = userRepository.findByMobileNo(user.getMobileNo());

            if(ObjectUtils.isEmpty(existingUser)) {
                User savedUser = userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK).body(userConvert.convert(savedUser));
            } else {
                throw new ApiException(Restaurant.ACCOUNT_ALREADY_EXIST);
            }

        } catch (Exception ex) {
            log.error("Error during user registration: {}", ex.getMessage());

            UserDto errorResponse = new UserDto();
            errorResponse.setStatus(HttpStatus.BAD_REQUEST);
            errorResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Override
    public ResponseEntity<UserDto> addAddress(UUID userId, AddressDto addressDto) {
        try {
            if (ObjectUtils.isEmpty(addressDto)) {
                throw new ApiException("Address details cannot be empty.");
            }
            User user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User not found with ID: " + userId));
            Address address = userConvert.convert(addressDto);
            user.getAddressList().add(address);
            User updatedUser = userRepository.save(user);
            UserDto updatedUserDto = userConvert.convert(updatedUser);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUserDto);
        } catch (Exception ex) {
            log.error("Error while adding address: {}", ex.getMessage());
            UserDto errorResponse = new UserDto();
            errorResponse.setStatus(HttpStatus.BAD_REQUEST);
            errorResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Override
    public ResponseEntity<UserDto> editProfile(UUID userId, UserDto userDto) {
        try {
            if (ObjectUtils.isEmpty(userDto)) {
                throw new ApiException("User details cannot be empty.");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException("User not found with ID: " + userId));

            user.setName(userDto.getName());
            user.setMobileNo(userDto.getMobileNo());
            user.setEmail(userDto.getEmail());
            user.setRole(UserType.valueOf(userDto.getRole().toUpperCase()));
//            user.setPassword(encoder.encode(userDto.getPassword()));

            if (userDto.getAddressList() != null) {
                List<Address> addresses = userDto.getAddressList()
                        .stream()
                        .map(userConvert::convert)
                        .collect(Collectors.toList());
                user.setAddressList(addresses);
            }

            User updatedUser = userRepository.save(user);
            UserDto updatedUserDto = userConvert.convert(updatedUser);

            return ResponseEntity.status(HttpStatus.OK).body(updatedUserDto);
        } catch (Exception ex) {
            log.error("Error while editing profile: {}", ex.getMessage());
            UserDto errorResponse = new UserDto();
            errorResponse.setStatus(HttpStatus.BAD_REQUEST);
            errorResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Override
    public ResponseEntity<UserDto> getProfile(UUID userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException("User not found with ID: " + userId));

            UserDto userDto = userConvert.convert(user);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (Exception ex) {
            log.error("Error while fetching profile: {}", ex.getMessage());
            UserDto errorResponse = new UserDto();
            errorResponse.setStatus(HttpStatus.BAD_REQUEST);
            errorResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    @Override
    public ResponseEntity<UserDto> updateUser(UUID userId, UserDto userDto) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException("User not found with ID: " + userId));

            if (userDto.getName() != null) {
                user.setName(userDto.getName());
            }
            if (userDto.getMobileNo() != null) {
                user.setMobileNo(userDto.getMobileNo());
            }
            if (userDto.getEmail() != null) {
                user.setEmail(userDto.getEmail());
            }
            if (userDto.getRole() != null) {
                user.setRole(UserType.valueOf(userDto.getRole().toUpperCase()));
            }
            if (userDto.getPassword() != null) {
                user.setPassword(userDto.getPassword());
            }

            User updatedUser = userRepository.save(user);
            UserDto updatedUserDto = userConvert.convert(updatedUser);

            return ResponseEntity.status(HttpStatus.OK).body(updatedUserDto);

        } catch (Exception e) {
            log.error("Error while updating user: {}", e.getMessage());
            UserDto error = new UserDto();
            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}