package com.spring.webflux.converter;

import com.spring.webflux.dto.request.address.CreateAddressDto;
import com.spring.webflux.dto.response.adress.AddressDto;
import com.spring.webflux.entity.Address;

import java.util.UUID;

public class AddressDtoConverter {

    public static AddressDto toDto(Address address) {
        return new AddressDto(address.getId(), address.getStreet(), address.getCity(),
                address.getCreatedAt(), address.getCustomerId());
    }

    public static Address toEntity(CreateAddressDto createAddressDto) {
        return Address.builder()
                .street(createAddressDto.street())
                .city(createAddressDto.city())
                .customerId(createAddressDto.customerId())
                .build();
    }
}
