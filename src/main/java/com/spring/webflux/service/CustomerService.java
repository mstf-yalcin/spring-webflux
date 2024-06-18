package com.spring.webflux.service;

import com.spring.webflux.converter.CustomerDtoConverter;
import com.spring.webflux.dto.request.customer.CreateCustomerDto;
import com.spring.webflux.dto.request.customer.UpdateCustomerDto;
import com.spring.webflux.dto.response.adress.AddressDto;
import com.spring.webflux.dto.response.customer.CustomerDto;
import com.spring.webflux.dto.response.customer.CustomerWithAddressDto;
import com.spring.webflux.entity.Customer;
import com.spring.webflux.exception.NotFoundException;
import com.spring.webflux.repository.CustomerRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final DatabaseClient databaseClient;

    public CustomerService(CustomerRepository customerRepository,
                           DatabaseClient databaseClient) {
        this.customerRepository = customerRepository;
        this.databaseClient = databaseClient;
    }

    public Flux<CustomerDto> getAll() {
        return customerRepository.findAll()
                .map(CustomerDtoConverter::toDto);
    }

    public Mono<CustomerDto> getById(UUID id) {
        return customerRepository.findById(id)
                .map(CustomerDtoConverter::toDto)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer not found:" + id)));
    }

//    public Mono<CustomerWithAddressDto> getCustomerByIdWithAddresses(UUID id) {
//        return databaseClient.sql("SELECT c.*,a.*,a.id as address_id FROM customer c INNER JOIN address a ON c.id = a.customer_id WHERE c.id = :id")
//                .bind("id", id)
//                .map((row, rowMetadata) -> new CustomerWithAddressDto(
//                        row.get("id", UUID.class),
//                        row.get("name", String.class),
//                        row.get("email", String.class),
//                        row.get("created_at", java.time.LocalDateTime.class),
//                        new AddressDto(
//                                row.get("address_id", UUID.class),
//                                row.get("street", String.class),
//                                row.get("city", String.class),
//                                row.get("customer_id", UUID.class)
//                        )
//                )).one();
//    }
    public Mono<CustomerWithAddressDto> getCustomerByIdWithAddresses(UUID id) {
        return databaseClient.sql("SELECT *, a.id as address_id, a.created_at as address_created_at  FROM customer c " +
                        "LEFT JOIN address a ON c.id = a.customer_id WHERE c.id = :id")
                .bind("id", id)
                .map((row, rowMetadata) -> {
                    UUID customerId = row.get("id", UUID.class);
                    String name = row.get("name", String.class);
                    String email = row.get("email", String.class);
                    LocalDateTime createdAt = row.get("created_at", LocalDateTime.class);

                    List<AddressDto> addresses = new ArrayList<>();
                    UUID addressId = row.get("address_id", UUID.class);
                    if (addressId != null) {
                        AddressDto address = new AddressDto(
                                addressId,
                                row.get("street", String.class),
                                row.get("city", String.class),
                                row.get("address_created_at", LocalDateTime.class),
                                row.get("customer_id", UUID.class)
                        );
                        addresses.add(address);
                    }
                    return new CustomerWithAddressDto(customerId, name, email, createdAt, addresses);
                })
                .all()
                .switchIfEmpty(Mono.error(new NotFoundException("Customer not found:" + id)))
                .single();
    }

    public Mono<CustomerDto> create(CreateCustomerDto createCustomerDto) {
        Customer newCustomer = CustomerDtoConverter.toEntity(createCustomerDto);
        newCustomer.setId(UUID.randomUUID());
        newCustomer.setAsNew();

        return customerRepository.save(newCustomer)
                .map(CustomerDtoConverter::toDto);
    }

    public Mono<CustomerDto> update(UpdateCustomerDto updateCustomerDto, UUID id) {
        return customerRepository.findById(id)
                .flatMap(customer -> {
                    customer.setName(updateCustomerDto.name());
                    customer.setEmail(updateCustomerDto.email());
                    return customerRepository.save(customer);
                })
                .map(CustomerDtoConverter::toDto)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer not found:" + id)));
    }

    public Mono<Void> delete(UUID id) {
        return customerRepository.deleteById(id);
    }

}

