package com.banking.account.api.controller;

import com.banking.account.application.service.AccountService;
import com.banking.account.dto.AccountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Integration test for AccountController.
 */
@WebFluxTest(AccountController.class)
class AccountControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AccountService accountService;

    @Test
    @DisplayName("Should create account successfully")
    void shouldCreateAccountSuccessfully() {
        AccountDto inputDto = AccountDto.builder()
                .accountNumber("123456")
                .accountType("Ahorro")
                .initialBalance(new BigDecimal("1000.00"))
                .customerId(1L)
                .build();

        AccountDto savedDto = AccountDto.builder()
                .accountId(1L)
                .accountNumber("123456")
                .accountType("Ahorro")
                .initialBalance(new BigDecimal("1000.00"))
                .status(true)
                .customerId(1L)
                .build();

        when(accountService.createAccount(any(AccountDto.class))).thenReturn(Mono.just(savedDto));

        webTestClient.post()
                .uri("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(inputDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.statusCode").isEqualTo(201)
                .jsonPath("$.status").isEqualTo("Created")
                .jsonPath("$.data.accountId").isEqualTo(1)
                .jsonPath("$.data.accountNumber").isEqualTo("123456");
    }
}
