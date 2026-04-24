package com.banking.account.config;

import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder.postConfigurer(mapper -> {
            mapper.coercionConfigFor(Integer.class)
                    .setCoercion(CoercionInputShape.String, CoercionAction.Fail);
            mapper.coercionConfigFor(Long.class)
                    .setCoercion(CoercionInputShape.String, CoercionAction.Fail);
        });
    }
}
