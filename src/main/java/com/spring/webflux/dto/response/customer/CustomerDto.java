package com.spring.webflux.dto.response.customer;

import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerDto(
        @Column("customer_id")
        UUID id,
        @Column("customer_name")
        String name,
        @Column("customer_email")
        String email,
        @Column("created_at")
        LocalDateTime createdAt) {
}
