package com.example.cosmocats.service;

import com.example.cosmocats.domain.Customer;
import com.example.cosmocats.dto.customer.CustomerRequestDto;
import com.example.cosmocats.repository.projection.CustomerDetailsProjection;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDetailsProjection> getAllCustomers();
    Customer getCustomerById(UUID id);
    Customer createCustomer(CustomerRequestDto customerRequestDto);
    void deleteCustomerById(UUID id);
}
