package com.example.cosmocats.web;

import com.example.cosmocats.AbstractIT;
import com.example.cosmocats.dto.customer.AddressRequestDto;
import com.example.cosmocats.dto.customer.CustomerRequestDto;
import com.example.cosmocats.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CustomerController IT")
@Tag("customer-service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerControllerIT extends AbstractIT {
    private static final Faker faker = new Faker(Locale.of("uk", "UA"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoSpyBean
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        reset(customerService);
    }

    private static CustomerRequestDto buildCustomerRequestDto() {
        return CustomerRequestDto.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.numerify("0#########"))
                .address(
                        AddressRequestDto.builder()
                                .country(faker.address().country())
                                .city(faker.address().city())
                                .street(faker.address().streetName())
                                .house(faker.address().buildingNumber())
                                .apartment(faker.numerify("##"))
                                .build()
                )
                .build();
    }

    private static Stream<CustomerRequestDto> provideCustomerRequests() {
        return Stream.of(
                buildCustomerRequestDto(),
                buildCustomerRequestDto(),
                buildCustomerRequestDto()
        );
    }

    @Order(1)
    @SneakyThrows
    @DisplayName("Should Save Customer")
    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("provideCustomerRequests")
    public void shouldSaveCustomer(CustomerRequestDto customerRequestDto) {
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(customerRequestDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customerRequestDto.getLastName()))
                .andExpect(jsonPath("$.email").value(customerRequestDto.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(customerRequestDto.getPhoneNumber()))
                .andExpect(jsonPath("$.address").exists());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should Return Conflict on Customer Email")
    public void shouldReturnConflictOnCustomerEmail() {
        String email = faker.internet().emailAddress();
        CustomerRequestDto customerRequestDto = buildCustomerRequestDto();
        customerRequestDto = customerRequestDto.toBuilder()
                .email(email)
                .build();
        customerService.createCustomer(customerRequestDto);

        CustomerRequestDto conflictCustomerRequestDto = buildCustomerRequestDto();
        conflictCustomerRequestDto = conflictCustomerRequestDto.toBuilder()
                .email(email)
                .build();

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(conflictCustomerRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("urn:problem-type:conflict-error"))
                .andExpect(jsonPath("$.title").value("Resource Already Exists Exception"))
                .andExpect(jsonPath("$.status").value("409"))
                .andExpect(jsonPath("$.detail").value(String.format("Customer with email '%s' already exists", email)))
                .andExpect(jsonPath("$.instance").value("/api/v1/customers"));
    }

    @Test
    @Order(2)
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should Get All Customers")
    public void shouldGetAllProducts() {
        mockMvc.perform(get("/api/v1/customers")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers").isArray())
                .andExpect(jsonPath("$.customers").isNotEmpty())
                .andExpect(jsonPath("$.customers[*].firstName").exists())
                .andExpect(jsonPath("$.customers[*].lastName").exists())
                .andExpect(jsonPath("$.customers[*].email").exists());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should Return 404 Not Found")
    public void shouldReturn404NotFound() {
        UUID wrongId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/customers/{id}", wrongId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("urn:problem-type:not-found"))
                .andExpect(jsonPath("$.title").value("Resource Not Found Exception"))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.detail").value(String.format("Customer with id '%s' not found", wrongId)))
                .andExpect(jsonPath("$.instance").value("/api/v1/customers/" + wrongId));
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should Get Customer By Id")
    public void shouldGetCustomerById() {
        CustomerRequestDto customerRequestDto = buildCustomerRequestDto();
        UUID id = customerService.createCustomer(customerRequestDto).getId();

        mockMvc.perform(get("/api/v1/customers/{id}", id)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value(customerRequestDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customerRequestDto.getLastName()))
                .andExpect(jsonPath("$.email").value(customerRequestDto.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(customerRequestDto.getPhoneNumber()))
                .andExpect(jsonPath("$.address").exists());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should Delete Customer")
    public void shouldDeleteCustomer() {
        mockMvc.perform(delete("/api/v1/customers/{id}", UUID.randomUUID())
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}
