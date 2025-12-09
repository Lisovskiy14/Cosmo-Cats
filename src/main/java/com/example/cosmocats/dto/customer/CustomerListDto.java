package com.example.cosmocats.dto.customer;

import com.example.cosmocats.repository.projection.CustomerDetailsProjection;
import lombok.Value;

import java.util.List;

@Value
public class CustomerListDto {
    List<CustomerDetailsProjection> customers;
}
