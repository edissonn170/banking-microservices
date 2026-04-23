package com.banking.customer.domain.model;

import lombok.Builder;
import lombok.Value;

/**
 * Domain model representing a Person.
 * Pure POJO without any framework dependencies.
 */
@Value
@Builder
public class Person {
    Long personId;
    String name;
    String gender;
    Integer age;
    String identification;
    String address;
    String phone;
}
