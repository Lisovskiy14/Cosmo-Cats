package com.example.cosmocats.web.mapper;

import com.example.cosmocats.domain.Address;
import com.example.cosmocats.domain.Customer;
import com.example.cosmocats.dto.customer.AddressDto;
import com.example.cosmocats.dto.customer.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CustomerWebMapper {
    @Mapping(target = "address", source = "address", qualifiedByName = "addressToAddressDto")
    CustomerDto toCustomerDto(Customer customer);

    @Named("addressToAddressDto")
    default AddressDto addressToAddressDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .apartment(address.getApartment())
                .build();
    }
}
