package com.restaurant.implementation;

import com.restaurant.constants.Restaurant;
import com.restaurant.convert.UserConvert;
import com.restaurant.dto.LoginDto;
import com.restaurant.dto.UserDto;
import com.restaurant.entity.User;
import com.restaurant.exception.ApiException;
import com.restaurant.repository.UserRepository;
import com.restaurant.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConvert userConvert;
//    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public ResponseEntity<UserDto> validateLogin(LoginDto loginDto) {
        try {
            User user = userRepository.findByMobileNo(loginDto.getMobileNo());

            if(ObjectUtils.isEmpty(user)) {
                throw new ApiException(Restaurant.USER_NOT_FOUND);
            }

            if(user.getPassword().equals(loginDto.getPassword())) {
                UserDto userDto = userConvert.convert(user);
                return ResponseEntity.status(HttpStatus.OK).body(userDto);
            } else {
                UserDto userDto = new UserDto();
                userDto.setMessage("wrong password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userDto);
            }
        } catch(Exception e) {
            log.info("Exception: {}", e.getMessage());
            UserDto userDto = new UserDto();
            userDto.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDto);
        }
    }
}

//    @Override
//    public ResponseEntity<UserDto> validateLogin(LoginDto loginDto) {
//        try {
//            log.info("{}", encoder.encode(loginDto.getPassword()));
//            User user = userRepository.findByMobileNo(loginDto.getMobileNo());
//
//            if(ObjectUtils.isEmpty(user)) {
//                throw new ApiException(Restaurant.USER_NOT_FOUND);
//            }
//
//            if(encoder.matches(loginDto.getPassword(), user.getPassword())) {
//                UserDto userDto = userConvert.convert(user);
//                return ResponseEntity.status(HttpStatus.OK).body(userDto);
//            } else {
//                UserDto userDto = new UserDto();
//                userDto.setMessage("wrong password");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDto);
//            }
//
//        } catch(Exception e) {
//            log.info("Exception: {}", e.getMessage());
//            UserDto userDto = new UserDto();
//            userDto.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDto);
//        }
//    }

