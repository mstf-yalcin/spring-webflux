package com.spring.webflux.service;

import com.spring.webflux.converter.AddressDtoConverter;
import com.spring.webflux.dto.request.address.CreateAddressDto;
import com.spring.webflux.dto.request.address.UpdateAddressDto;
import com.spring.webflux.dto.response.adress.AddressDto;
import com.spring.webflux.entity.Address;
import com.spring.webflux.exception.NotFoundException;
import com.spring.webflux.repository.AddressRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Flux<AddressDto> getAll() {
        return addressRepository.findAll()
                .map(AddressDtoConverter::toDto);
    }

    public Mono<AddressDto> getById(UUID id) {
        return addressRepository.findById(id)
                .map(AddressDtoConverter::toDto)
                .switchIfEmpty(Mono.error(new NotFoundException("Address not found:" + id)));
    }

    public Mono<AddressDto> create(CreateAddressDto createAddressDto) {
        Address newAddress = AddressDtoConverter.toEntity(createAddressDto);
        newAddress.setId(UUID.randomUUID());
        newAddress.setAsNew();
        return addressRepository.save(newAddress)
                .map(AddressDtoConverter::toDto);
    }

    public Mono<AddressDto> update(UpdateAddressDto updateAddressDto, UUID id) {
        return addressRepository.findById(id)
                .flatMap(address -> {
                    address.setCity(updateAddressDto.city());
                    address.setStreet(updateAddressDto.street());
                    address.setCustomerId(updateAddressDto.customerId());
                    return addressRepository.save(address);
                }).map(AddressDtoConverter::toDto)
                .switchIfEmpty(Mono.error(new NotFoundException("Address not found:" + id)));
    }

    public Mono<Void> delete(UUID id) {
        return addressRepository.deleteById(id);
    }
}
