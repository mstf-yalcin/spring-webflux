package com.spring.webflux.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public WebProperties.Resources webResources() {
        return new WebProperties.Resources();
    }
}
