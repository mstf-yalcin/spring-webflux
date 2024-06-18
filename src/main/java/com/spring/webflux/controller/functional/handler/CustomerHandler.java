package com.spring.webflux.controller.functional.handler;


import com.spring.webflux.dto.request.customer.CreateCustomerDto;
import com.spring.webflux.dto.request.customer.UpdateCustomerDto;
import com.spring.webflux.dto.response.customer.CustomerDto;
import com.spring.webflux.exception.GlobalRequestValidator;
import com.spring.webflux.service.CustomerService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@Component
public class CustomerHandler {
    private static final String CUSTOMER_API_PREFIX = "/api/v2/customer";
    private final CustomerService customerService;

    private final GlobalRequestValidator requestValidator;

    public CustomerHandler(CustomerService customerService, GlobalRequestValidator requestValidator) {
        this.customerService = customerService;
        this.requestValidator = requestValidator;
    }

    public Mono<ServerResponse> handleCreate(ServerRequest request) {
        Mono<CreateCustomerDto> createCustomerDtoMono = request.bodyToMono(CreateCustomerDto.class);
        return createCustomerDtoMono
                .flatMap(this.requestValidator::validateRequest)
                .flatMap(customerService::create)
                .flatMap(customer -> ServerResponse.created(URI.create(CUSTOMER_API_PREFIX + "/" + customer.id()))
                        .bodyValue(customer))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> handleGetAll(ServerRequest request) {
        return ServerResponse.ok()
                .body(customerService.getAll(), CustomerDto.class);
    }

    public Mono<ServerResponse> handleStreamGetAll(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
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
        UUID uuid = UUID.fromString(id);
        return customerService.getById(uuid)
                .flatMap(customerDto -> ServerResponse.ok().bodyValue(customerDto));
    }

    public Mono<ServerResponse> handleUpdate(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<UpdateCustomerDto> updateCustomerDtoMono = request.bodyToMono(UpdateCustomerDto.class);

        return updateCustomerDtoMono
                .flatMap(this.requestValidator::validateRequest)
                .flatMap(updateCustomerDto -> customerService.update(updateCustomerDto, UUID.fromString(id))
                        .flatMap(customerDto -> ServerResponse.ok().bodyValue(customerDto)));
    }

    public Mono<ServerResponse> handleDelete(ServerRequest request) {
        String id = request.pathVariable("id");
        return customerService.delete(UUID.fromString(id))
                .then(ServerResponse.noContent().build());
    }
}
