package com.example.fantasta.auction_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient authRestClient(@Value("${auth.service.url}") String authServiceUrl) {
        return RestClient.builder()
                .baseUrl(authServiceUrl)
                .build();
    }
}