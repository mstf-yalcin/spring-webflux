package com.spring.webflux.dto.request.customer;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerDto(@NotBlank String name,@NotBlank String email) {
}
