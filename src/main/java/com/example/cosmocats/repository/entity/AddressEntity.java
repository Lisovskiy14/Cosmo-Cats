package com.example.cosmocats.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Entity
@Table(name = "customer_addresses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {

    @Id
    @Column(name = "customer_id")
    private UUID customerId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    private String country;
    private String city;
    private String street;
    private String house;
    private String apartment;
}
