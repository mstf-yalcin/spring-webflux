package com.spring.webflux.dto.response.customer;

import com.spring.webflux.dto.response.adress.AddressDto;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CustomerWithAddressDto(
        @Column("customer_id")
        UUID id,
        @Column("customer_name")
        String name,
        @Column("customer_email")
        String email,
        @Column("created_at")
        LocalDateTime createdAt,
        List<AddressDto> address) {
}
