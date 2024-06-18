package com.spring.webflux.dto.request.address;

import com.spring.webflux.validation.annotation.ValidUUID;

import java.util.UUID;

public record UpdateAddressDto(String street, String city, @ValidUUID UUID customerId) {
}
