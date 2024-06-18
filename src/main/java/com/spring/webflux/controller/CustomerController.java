package com.spring.webflux.controller;

import com.spring.webflux.dto.request.customer.CreateCustomerDto;
import com.spring.webflux.dto.request.customer.UpdateCustomerDto;
import com.spring.webflux.dto.response.customer.CustomerDto;
import com.spring.webflux.dto.response.customer.CustomerWithAddressDto;
import com.spring.webflux.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final WebClient webClient;
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.webClient = WebClient.create();
        this.customerService = customerService;
    }

    @GetMapping("{id}")
    public Mono<CustomerDto> getById(@PathVariable UUID id) {
        return customerService.getById(id);
    }

    @GetMapping("{id}/addresses")
    public Mono<CustomerWithAddressDto> getCustomerByIdWithAddresses(@PathVariable UUID id) {
        return customerService.getCustomerByIdWithAddresses(id);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CustomerDto> getAll() {
        return customerService.getAll()
                .delayElements(Duration.ofSeconds(1));
    }

    @GetMapping
    public Flux<CustomerDto> getAllStream() {
        return customerService.getAll();
    }

    @PostMapping
    public Mono<CustomerDto> create(@RequestBody @Valid CreateCustomerDto customerDto) {
        return customerService.create(customerDto);
    }

    @PutMapping("{id}")
    public Mono<CustomerDto> update(@RequestBody @Valid UpdateCustomerDto updateCustomerDto, @PathVariable UUID id) {
        return customerService.update(updateCustomerDto, id);
    }

    @DeleteMapping("{id}")
    public Mono<Void> delete(@PathVariable UUID id) {
        return customerService.delete(id);
    }
}
