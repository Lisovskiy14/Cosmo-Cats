package com.example.cosmocats.service;

import com.example.cosmocats.domain.Customer;
import com.example.cosmocats.dto.customer.CustomerRequestDto;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerById(UUID id);
    Customer createCustomer(CustomerRequestDto customerRequestDto);
    void deleteCustomerById(UUID id);
}
