package com.api.gamesapi.configuration;

import org.aspectj.lang.annotation.Before;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }
}
