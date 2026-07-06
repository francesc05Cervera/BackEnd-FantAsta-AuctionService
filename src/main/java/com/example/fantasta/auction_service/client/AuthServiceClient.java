package com.example.fantasta.auction_service.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.dto.AuthUserResponse;

@Service
public class AuthServiceClient {

    private final RestClient authRestClient;

    public AuthServiceClient(RestClient authRestClient) 
    {
        this.authRestClient = authRestClient;
    }

    public AuthUserResponse getAuthenticatedUser(String authorizationHeader) throws TokenException
    {
        try {
            return authRestClient.get()
                    .uri("/api/user/me")
                    .header("Authorization", authorizationHeader)
                    .retrieve()
                    .body(AuthUserResponse.class);
        } catch (RestClientResponseException e) {
            throw new TokenException("Errore nella verifica del token con auth-service: " + e.getStatusCode());
        }
    }
}