package com.spring.webflux.dto.request.customer;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerDto(@NotBlank String name,@NotBlank String email) {
}
