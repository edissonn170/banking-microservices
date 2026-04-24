package com.banking.customer.application.service;

import com.banking.customer.application.exception.CustomerAlreadyExistsException;
import com.banking.customer.application.mapper.CustomerDtoMapper;
import com.banking.customer.domain.repository.CustomerRepository;
import com.banking.customer.domain.repository.PersonRepository;
import com.banking.customer.dto.CustomerDto;
import com.banking.customer.dto.PersonDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CustomerDtoMapper customerDtoMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("Should throw CustomerAlreadyExistsException when identification already exists")
    void shouldThrowExceptionWhenIdentificationAlreadyExists() {
        PersonDto personDto = PersonDto.builder()
                .name("Edison Narváez")
                .gender("M")
                .age(28)
                .identification("1726270431")
                .build();

        CustomerDto dto = CustomerDto.builder()
                .person(personDto)
                .password("12345")
                .build();

        when(personRepository.existsByIdentification("1726270431")).thenReturn(Mono.just(true));

        StepVerifier.create(customerService.createCustomer(dto))
                .expectError(CustomerAlreadyExistsException.class)
                .verify();
    }
}
