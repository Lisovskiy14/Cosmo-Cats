package com.example.cosmocats.service.impl;

import com.example.cosmocats.domain.Customer;
import com.example.cosmocats.dto.customer.CustomerRequestDto;
import com.example.cosmocats.repository.CustomerRepository;
import com.example.cosmocats.repository.entity.AddressEntity;
import com.example.cosmocats.repository.entity.CustomerEntity;
import com.example.cosmocats.repository.projection.CustomerDetailsProjection;
import com.example.cosmocats.service.CustomerService;
import com.example.cosmocats.service.exception.conflict.CustomerEmailAlreadyExistsException;
import com.example.cosmocats.service.exception.conflict.CustomerPhoneNumberAlreadyExistsException;
import com.example.cosmocats.service.exception.notFound.CustomerNotFoundException;
import com.example.cosmocats.service.mapper.CustomerEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerEntityMapper customerEntityMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<CustomerDetailsProjection> getAllCustomers() {
        return customerRepository.findAllBy();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Customer getCustomerById(UUID id) {
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));
        return customerEntityMapper.toCustomer(customerEntity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Customer createCustomer(CustomerRequestDto customerRequestDto) {
        CustomerEntity conflictCustomer = customerRepository.findByEmailOrPhoneNumber(
                customerRequestDto.getEmail(),
                customerRequestDto.getPhoneNumber()
        );

        if (conflictCustomer != null) {
            if (conflictCustomer.getEmail().equals(customerRequestDto.getEmail())) {
                throw new CustomerEmailAlreadyExistsException(customerRequestDto.getEmail());
            } else if (conflictCustomer.getPhoneNumber().equals(customerRequestDto.getPhoneNumber())) {
                throw new CustomerPhoneNumberAlreadyExistsException(customerRequestDto.getPhoneNumber());
            }
        }

        CustomerEntity customerEntity = CustomerEntity.builder()
                .firstName(customerRequestDto.getFirstName())
                .lastName(customerRequestDto.getLastName())
                .email(customerRequestDto.getEmail())
                .phoneNumber(customerRequestDto.getPhoneNumber())
                .build();

        customerEntity.setAddress(
                AddressEntity.builder()
                        .customer(customerEntity)
                        .country(customerRequestDto.getAddress().getCountry())
                        .city(customerRequestDto.getAddress().getCity())
                        .street(customerRequestDto.getAddress().getStreet())
                        .house(customerRequestDto.getAddress().getHouse())
                        .apartment(customerRequestDto.getAddress().getApartment())
                        .build()
        );

        CustomerEntity savedCustomerEntity = customerRepository.save(customerEntity);
        log.info("New customer with id '{}' has been created", savedCustomerEntity.getId());
        return customerEntityMapper.toCustomer(savedCustomerEntity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteCustomerById(UUID id) {
        customerRepository.deleteById(id);
        log.info("Customer with id '{}' has been deleted", id);
    }
}
