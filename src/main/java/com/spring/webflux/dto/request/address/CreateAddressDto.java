package com.spring.webflux.dto.request.address;

import com.spring.webflux.validation.annotation.ValidUUID;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateAddressDto(@NotBlank String street, @NotBlank String city, @ValidUUID UUID customerId) {
}
