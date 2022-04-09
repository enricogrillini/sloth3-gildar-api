package it.eg.sloth.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.eg.sloth.api.common.ObjectMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    ObjectMapper objectMapper() {
        return ObjectMapperFactory.objectMapper();
    }

}
