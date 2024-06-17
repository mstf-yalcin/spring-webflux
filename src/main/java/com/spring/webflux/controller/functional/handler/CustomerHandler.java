package com.spring.webflux.controller.functional.handler;


import com.spring.webflux.dto.request.customer.CreateCustomerDto;
import com.spring.webflux.dto.request.customer.UpdateCustomerDto;
import com.spring.webflux.dto.response.customer.CustomerDto;
import com.spring.webflux.service.CustomerService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CustomerHandler {
    private final CustomerService customerService;

    public CustomerHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Mono<ServerResponse> handleCreate(ServerRequest request) {
        Mono<CreateCustomerDto> createCustomerDtoMono = request.bodyToMono(CreateCustomerDto.class);
        return createCustomerDtoMono
                .flatMap(customerService::create)
                .flatMap(customer -> ServerResponse.created(null).bodyValue(customer))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> handleGetAll(ServerRequest request) {
        return ServerResponse.ok()
                .body(customerService.getAll(), CustomerDto.class);
    }

    public Mono<ServerResponse> handleGetAllWithAddresses(ServerRequest request) {
        String id = request.pathVariable("id");
        return customerService.getCustomerByIdWithAddresses(UUID.fromString(id))
                .flatMap(customerWithAddresses ->
                        ServerResponse.ok().bodyValue(customerWithAddresses));
    }

    public Mono<ServerResponse> handleGetById(ServerRequest request) {
        String id = request.pathVariable("id");
        try {
            UUID uuid = UUID.fromString(id);
            return customerService.getById(uuid)
                    .flatMap(customerDto -> ServerResponse.ok().bodyValue(customerDto));
        } catch (IllegalArgumentException e) {
            return ServerResponse.badRequest().bodyValue("Invalid UUID format: " + id);
        }
    }

    public Mono<ServerResponse> handleUpdate(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<UpdateCustomerDto> updateCustomerDtoMono = request.bodyToMono(UpdateCustomerDto.class);

        return updateCustomerDtoMono
                .flatMap(updateCustomerDto -> customerService.update(updateCustomerDto, UUID.fromString(id))
                        .flatMap(customerDto -> ServerResponse.ok().bodyValue(customerDto)));
    }

    public Mono<ServerResponse> handleDelete(ServerRequest request) {
        String id = request.pathVariable("id");
        return customerService.delete(UUID.fromString(id))
                .then(ServerResponse.noContent().build());
    }

}
