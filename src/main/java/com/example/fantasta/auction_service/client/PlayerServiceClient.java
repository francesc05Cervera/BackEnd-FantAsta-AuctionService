package com.example.fantasta.auction_service.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.example.fantasta.auction_service.dto.PlayerDTO;
import com.example.fantasta.auction_service.exception.NotFoundException;

@Service
public class PlayerServiceClient {
    private final RestClient playerRestClient;
    public PlayerServiceClient(RestClient playerRestClient) { this.playerRestClient = playerRestClient; }

    public List<PlayerDTO> getPlayers(String header) { return request("/api/players", header, new ParameterizedTypeReference<>() {}); }

    public PlayerDTO getPlayer(String header, Integer playerId) {
        try {
            return playerRestClient.get().uri("/api/players/{id}", playerId).header("Authorization", header).retrieve().body(PlayerDTO.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) throw new NotFoundException("Player not found");
            throw upstream(exception);
        } catch (ResourceAccessException exception) { throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "PlayerService is unavailable", exception); }
        catch (RestClientException exception) { throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Invalid response from PlayerService", exception); }
    }

    private <T> T request(String uri, String header, ParameterizedTypeReference<T> type) {
        try { return playerRestClient.get().uri(uri).header("Authorization", header).retrieve().body(type); }
        catch (RestClientResponseException exception) { throw upstream(exception); }
        catch (ResourceAccessException exception) { throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "PlayerService is unavailable", exception); }
        catch (RestClientException exception) { throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Invalid response from PlayerService", exception); }
    }

    private ResponseStatusException upstream(RestClientResponseException exception) {
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "PlayerService returned an error", exception);
    }
}
