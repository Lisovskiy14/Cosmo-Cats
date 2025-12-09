package com.example.cosmocats.dto.customer;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CustomerDto {
    String id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    AddressDto address;
}
