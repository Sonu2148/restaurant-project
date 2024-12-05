package com.restaurant.convert;

import com.restaurant.dto.AddressDto;
import com.restaurant.dto.UserDto;
import com.restaurant.entity.Address;
import com.restaurant.entity.User;
import com.restaurant.enums.UserType;
import com.restaurant.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConvert {

    public User convert(UserDto userDto) {
        User user = new User();
        if (!ObjectUtils.isEmpty(userDto)) {
            BeanUtils.copyProperties(userDto, user);

            if(!StringUtils.isEmpty(userDto.getRole())) {
                user.setRole(UserType.valueOf(userDto.getRole().toUpperCase()));
            } else {
                throw new ApiException("Role is a must available field");
            }

            if (userDto.getAddressList() != null) {
                List<Address> addresses = userDto.getAddressList()
                        .stream()
                        .map(addressDto -> this.convert(addressDto)) // Avoid ambiguity
                        .collect(Collectors.toList());
                user.setAddressList(addresses);
            }
        }
        return user;
    }

    public UserDto convert(User user) {
        UserDto userDto = new UserDto();
        if (!ObjectUtils.isEmpty(user)) {
            BeanUtils.copyProperties(user, userDto);

            if(!ObjectUtils.isEmpty(user.getRole())) {
                userDto.setRole(user.getRole().toString());
            }

            if (user.getAddressList() != null) {
                List<AddressDto> addressDtos = user.getAddressList()
                        .stream()
                        .map(this::convert) // Calls the Address-to-AddressDto method
                        .collect(Collectors.toList());
                userDto.setAddressList(addressDtos);
            }
        }
        return userDto;
    }

    public Address convert(AddressDto addressDto) {
        Address address = new Address();
        if (!ObjectUtils.isEmpty(addressDto)) {
            BeanUtils.copyProperties(addressDto, address);
        }
        return address;
    }

    public AddressDto convert(Address address) {
        AddressDto addressDto = new AddressDto();
        if (!ObjectUtils.isEmpty(address)) {
            BeanUtils.copyProperties(address, addressDto);
        }
        return addressDto;
    }
}