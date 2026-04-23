package com.banking.customer.application.mapper;

import com.banking.customer.domain.model.Person;
import com.banking.customer.dto.PersonDto;
import org.springframework.stereotype.Component;

@Component
public class PersonDtoMapper {

    public PersonDto toDto(Person person) {
        if (person == null) {
            return null;
        }
        return PersonDto.builder()
                .personId(person.getPersonId())
                .name(person.getName())
                .gender(person.getGender())
                .age(person.getAge())
                .identification(person.getIdentification())
                .address(person.getAddress())
                .phone(person.getPhone())
                .build();
    }

    public Person toDomain(PersonDto dto) {
        if (dto == null) {
            return null;
        }
        return Person.builder()
                .personId(dto.getPersonId())
                .name(dto.getName())
                .gender(dto.getGender())
                .age(dto.getAge())
                .identification(dto.getIdentification())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .build();
    }
}
