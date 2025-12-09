package com.example.cosmocats.dto.customer;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AddressDto {
    String country;
    String city;
    String street;
    String house;
    String apartment;
}
