package com.example.fantasta.auction_service.client;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
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

    public AuthUserResponse getAuthenticatedUser(String authorizationHeader) throws TokenException {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new TokenException("Missing Authorization header");
        }

        try {
            AuthUserResponse user = authRestClient.get()
                    .uri("/api/user/me")
                    .header("Authorization", authorizationHeader)
                    .retrieve()
                    .body(AuthUserResponse.class);
            if (user == null || user.getId() == null || user.getId() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Invalid response from AuthService");
            }
            return user;
        } catch (RestClientResponseException e) {
            int status = e.getStatusCode().value();
            if (status == 400 || status == 401 || status == 403 || status == 404) {
                throw new TokenException("Invalid or expired token");
            }
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AuthService returned an error", e);
        } catch (ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "AuthService is unavailable", e);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Invalid response from AuthService", e);
        }
    }
}
