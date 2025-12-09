package com.example.cosmocats.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Customer {
    UUID id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    Address address;
}
