package com.spring.webflux.controller.functional.handler;

import com.spring.webflux.dto.request.address.CreateAddressDto;
import com.spring.webflux.dto.request.address.UpdateAddressDto;
import com.spring.webflux.dto.response.adress.AddressDto;
import com.spring.webflux.service.AddressService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class AddressHandler {
    private final AddressService addressService;

    public AddressHandler(AddressService addressService) {
        this.addressService = addressService;
    }

    public Mono<ServerResponse> handleCreate(ServerRequest request) {
        Mono<CreateAddressDto> createAddressDtoMono = request.bodyToMono(CreateAddressDto.class);

        return createAddressDtoMono
                .flatMap(addressService::create)
                .flatMap(address -> ServerResponse.created(null).bodyValue(address));
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
                .flatMap(updateAddressDto -> addressService.update(updateAddressDto, UUID.fromString(id)))
                .flatMap(addressDto -> ServerResponse.ok().bodyValue(addressDto));

    }

    public Mono<ServerResponse> handleDelete(ServerRequest request) {
        String id = request.pathVariable("id");

        return addressService.delete(UUID.fromString(id))
                .then(ServerResponse.noContent().build());
    }

}
