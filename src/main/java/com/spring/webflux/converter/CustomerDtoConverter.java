package com.spring.webflux.converter;

import com.spring.webflux.dto.request.customer.CreateCustomerDto;
import com.spring.webflux.dto.response.customer.CustomerDto;
import com.spring.webflux.entity.Customer;

public class CustomerDtoConverter {

    public static CustomerDto toDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getName(),
                customer.getEmail(), customer.getCreatedAt());
    }

    public static Customer toEntity(CreateCustomerDto createCustomerDto) {
        return Customer.builder()
                .name(createCustomerDto.name())
                .email(createCustomerDto.email())
                .build();
    }
}
