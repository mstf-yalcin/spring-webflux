package com.spring.webflux.controller.functional.handler;

import com.spring.webflux.dto.request.address.CreateAddressDto;
import com.spring.webflux.dto.request.address.UpdateAddressDto;
import com.spring.webflux.dto.response.adress.AddressDto;
import com.spring.webflux.exception.GlobalRequestValidator;
import com.spring.webflux.service.AddressService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@Component
public class AddressHandler {
    private static final String ADDRESS_API_PREFIX = "/api/v2/address";
    private final AddressService addressService;
    private final GlobalRequestValidator requestValidator;

    public AddressHandler(AddressService addressService, GlobalRequestValidator requestValidator) {
        this.addressService = addressService;
        this.requestValidator = requestValidator;
    }

    public Mono<ServerResponse> handleCreate(ServerRequest request) {
        Mono<CreateAddressDto> createAddressDtoMono = request.bodyToMono(CreateAddressDto.class);

        return createAddressDtoMono
                .flatMap(this.requestValidator::validateRequest)
                .flatMap(addressService::create)
                .flatMap(address -> ServerResponse.created(URI.create(ADDRESS_API_PREFIX + "/" + address.id()))
                        .bodyValue(address));
    }

    public Mono<ServerResponse> handleGetAll(ServerRequest request) {
        return ServerResponse.ok()
                .body(addressService.getAll(), AddressDto.class);
    }

    public Mono<ServerResponse> handleGetById(ServerRequest request) {
        String id = request.pathVariable("id");

        return addressService.getById(UUID.fromString(id))
                .flatMap(addressDto -> ServerResponse.ok().bodyValue(addressDto));
    }

    public Mono<ServerResponse> handleUpdate(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<UpdateAddressDto> updateAddressDtoMono = request.bodyToMono(UpdateAddressDto.class);

        return updateAddressDtoMono
                .flatMap(this.requestValidator::validateRequest)
                .flatMap(updateAddressDto -> addressService.update(updateAddressDto, UUID.fromString(id)))
                .flatMap(addressDto -> ServerResponse.ok().bodyValue(addressDto));

    }

    public Mono<ServerResponse> handleDelete(ServerRequest request) {
        String id = request.pathVariable("id");

        return addressService.delete(UUID.fromString(id))
                .then(ServerResponse.noContent().build());
    }

}
