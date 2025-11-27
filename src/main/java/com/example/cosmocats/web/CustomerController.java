package com.example.cosmocats.web;

import com.example.cosmocats.dto.customer.CustomerDto;
import com.example.cosmocats.dto.customer.CustomerListDto;
import com.example.cosmocats.dto.customer.CustomerRequestDto;
import com.example.cosmocats.repository.projection.CustomerDetailsProjection;
import com.example.cosmocats.service.CustomerService;
import com.example.cosmocats.web.mapper.CustomerWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerWebMapper customerWebMapper;

    @GetMapping
    public ResponseEntity<CustomerListDto> getAllCustomers() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CustomerListDto(
                        customerService.getAllCustomers()));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable UUID customerId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerWebMapper.toCustomerDto(
                        customerService.getCustomerById(customerId)));
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Valid CustomerRequestDto customerRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerWebMapper.toCustomerDto(
                        customerService.createCustomer(customerRequestDto)));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable UUID customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.noContent()
                .build();
    }
}
