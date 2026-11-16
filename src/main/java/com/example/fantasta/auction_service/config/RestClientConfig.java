package com.example.fantasta.auction_service.config;

import org.springframework.beans.factory.annotation.Value;
import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient authRestClient(@Value("${auth.service.url}") String authServiceUrl) {
        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
        JdkClientHttpRequestFactory requests = new JdkClientHttpRequestFactory(httpClient);
        requests.setReadTimeout(Duration.ofSeconds(5));
        return RestClient.builder()
                .requestFactory(requests)
                .baseUrl(authServiceUrl)
                .build();
    }

    @Bean
    public RestClient playerRestClient(@Value("${player.service.url}") String playerServiceUrl) {
        return RestClient.builder()
                .baseUrl(playerServiceUrl)
                .build();
    }
}