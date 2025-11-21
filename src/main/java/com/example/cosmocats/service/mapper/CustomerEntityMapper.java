package com.example.cosmocats.service.mapper;

import com.example.cosmocats.domain.Address;
import com.example.cosmocats.domain.Customer;
import com.example.cosmocats.repository.entity.AddressEntity;
import com.example.cosmocats.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {
    @Mapping(target = "address", source = "address", qualifiedByName = "addressEntityToAddress")
    Customer toCustomer(CustomerEntity customerEntity);

    private Address addressEntityToAddress(AddressEntity addressEntity) {
        return Address.builder()
                .country(addressEntity.getCountry())
                .city(addressEntity.getCity())
                .street(addressEntity.getStreet())
                .house(addressEntity.getHouse())
                .apartment(addressEntity.getApartment())
                .build();
    }
}
