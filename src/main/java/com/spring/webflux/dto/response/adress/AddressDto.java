package com.spring.webflux.dto.response.adress;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.webflux.dto.response.customer.CustomerDto;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

public record AddressDto(
        UUID id, String street, String city, LocalDateTime createdAt,
//        CustomerDto customer,
        @JsonIgnore @Column("customer_id") UUID customerId
//        @JsonIgnore @Column("customer_name") String customerName,
//        @JsonIgnore @Column("customer_email") String customerEmail
) {
}
