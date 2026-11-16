package com.example.fantasta.auction_service.client;

import com.example.fantasta.auction_service.exception.TokenException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

class AuthServiceClientTests {
    private final RestClient.Builder builder = RestClient.builder().baseUrl("http://auth.test");
    private final MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
    private final AuthServiceClient client = new AuthServiceClient(builder.build());

    @Test
    void forwardsBearerAndDeserializesCurrentUser() throws Exception {
        server.expect(requestTo("http://auth.test/api/user/me"))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer example"))
                .andRespond(withSuccess("""
                        {"id":5,"email":"mario@example.test","name":"Mario","surname":"Rossi"}
                        """, MediaType.APPLICATION_JSON));

        var user = client.getAuthenticatedUser("Bearer example");
        assertEquals(5L, user.getId());
        assertEquals("mario@example.test", user.getEmail());
        server.verify();
    }

    @Test
    void rejectsInvalidCredentials() {
        server.expect(requestTo("http://auth.test/api/user/me")).andRespond(withUnauthorizedRequest());
        assertThrows(TokenException.class, () -> client.getAuthenticatedUser("Bearer expired"));
        server.verify();
    }

    @Test
    void distinguishesAuthServerErrorsFromInvalidCredentials() {
        server.expect(requestTo("http://auth.test/api/user/me")).andRespond(withServerError());
        var error = assertThrows(ResponseStatusException.class,
                () -> client.getAuthenticatedUser("Bearer example"));
        assertEquals(HttpStatus.BAD_GATEWAY, error.getStatusCode());
        server.verify();
    }

    @Test
    void handlesUnreachableAuthService() {
        server.expect(requestTo("http://auth.test/api/user/me"))
                .andRespond(withException(new IOException("Connection refused")));
        var error = assertThrows(ResponseStatusException.class,
                () -> client.getAuthenticatedUser("Bearer example"));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, error.getStatusCode());
        server.verify();
    }
}
