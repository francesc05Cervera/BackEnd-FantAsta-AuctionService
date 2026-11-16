package com.example.fantasta.auction_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient authRestClient(@Value("${auth.service.url}") String authServiceUrl) {
        SimpleClientHttpRequestFactory requests = new SimpleClientHttpRequestFactory();
        requests.setConnectTimeout(2000);
        requests.setReadTimeout(5000);
        return RestClient.builder()
                .requestFactory(requests)
                .baseUrl(authServiceUrl)
                .build();
    }

    @Bean
    public RestClient playerRestClient(@Value("${player.service.url}") String playerServiceUrl) {
        SimpleClientHttpRequestFactory requests = new SimpleClientHttpRequestFactory();
        requests.setConnectTimeout(2000);
        requests.setReadTimeout(5000);
        return RestClient.builder()
                .requestFactory(requests)
                .baseUrl(playerServiceUrl)
                .build();
    }
}
