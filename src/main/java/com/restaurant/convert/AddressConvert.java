package com.restaurant.convert;

import com.restaurant.dto.AddressDto;
import com.restaurant.entity.Address;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Component
public class AddressConvert {
    public Address convert(AddressDto addressDto) {
        Address address = new Address();

        if(!ObjectUtils.isEmpty(addressDto)) {
            BeanUtils.copyProperties(addressDto, address);
        }

        return address;
    }
}
