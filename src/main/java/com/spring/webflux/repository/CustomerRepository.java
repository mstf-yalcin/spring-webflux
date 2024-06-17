package com.spring.webflux.repository;

import com.spring.webflux.entity.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface CustomerRepository extends R2dbcRepository<Customer, UUID> {
}
