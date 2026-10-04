package com.trezi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient aiServiceRestClient(@Value("${trezi.ai-service.base-url:http://localhost:8000}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}
