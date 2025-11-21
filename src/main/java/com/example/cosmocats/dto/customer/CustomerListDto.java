package com.example.cosmocats.dto.customer;

import lombok.Value;

import java.util.List;

@Value
public class CustomerListDto {
    List<CustomerDto> customers;
}
