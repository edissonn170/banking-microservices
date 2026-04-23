package com.banking.customer.client;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for customer client module.
 */
@Configuration
@ComponentScan(basePackages = "com.banking.customer.client") // search client
public class CustomerClientConfig {
}
