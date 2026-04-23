package com.banking.customer.infrastructure.mapper;

import com.banking.customer.domain.model.Person;
import com.banking.customer.infrastructure.entity.PersonEntity;
import org.springframework.stereotype.Component;

@Component
public class PersonEntityMapper {

    public Person toDomain(PersonEntity entity) {
        if (entity == null) {
            return null;
        }
        return Person.builder()
                .personId(entity.getPersonId())
                .name(entity.getName())
                .gender(entity.getGender())
                .age(entity.getAge())
                .identification(entity.getIdentification())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .build();
    }

    public PersonEntity toEntity(Person domain) {
        if (domain == null) {
            return null;
        }
        return PersonEntity.builder()
                .personId(domain.getPersonId())
                .name(domain.getName())
                .gender(domain.getGender())
                .age(domain.getAge())
                .identification(domain.getIdentification())
                .address(domain.getAddress())
                .phone(domain.getPhone())
                .build();
    }
}
