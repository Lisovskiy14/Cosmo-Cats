package com.example.cosmocats.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Address {
    String country;
    String city;
    String street;
    String house;
    String apartment;
}
