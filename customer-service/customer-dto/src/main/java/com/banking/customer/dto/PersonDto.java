package com.banking.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDto {

    Long personId;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name;

    @NotNull(message = "Gender is required")
    @Pattern(regexp = "^(M|F)$", message = "Gender must be 'M' or 'F'")
    String gender;

    @NotNull(message = "Age is required")
    @Min(value = 16, message = "Age must be at least 16")
    @Max(value = 150, message = "Age must be a realistic value")
    Integer age;

    @NotBlank(message = "Identification is required")
    @Size(max = 20, message = "Identification must not exceed 20 characters")
    String identification;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    String address;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    String phone;
}
