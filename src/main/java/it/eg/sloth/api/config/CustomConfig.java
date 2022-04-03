package it.eg.sloth.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eg.sloth.api.common.ObjectMapperFactory;
import org.springframework.context.annotation.Bean;

public class CustomConfig {

    @Bean
    ObjectMapper objectMapper () {
        return ObjectMapperFactory.objectMapper();
    }
}
