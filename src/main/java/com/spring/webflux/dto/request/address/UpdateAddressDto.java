package com.spring.webflux.dto.request.address;

import java.util.UUID;

public record UpdateAddressDto(String street, String city, UUID customerId) {
}
