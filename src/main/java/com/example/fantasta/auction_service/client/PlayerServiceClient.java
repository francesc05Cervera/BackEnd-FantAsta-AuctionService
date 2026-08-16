package com.example.fantasta.auction_service.client;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.example.fantasta.auction_service.dto.PlayerDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;


@Service
public class PlayerServiceClient 
{
    private final RestClient playerRestClient;  // nome corretto
    @Value("${player.service.url}")
    private String url;
    public PlayerServiceClient(RestClient playerRestClient) {
        this.playerRestClient = playerRestClient;
    }
 public List<PlayerDTO> getPlayers(String authorizationHeader) 
    {
        String url = this.url + "/api/players";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        return playerRestClient.get()
                .uri(url)
                .header("Authorization", authorizationHeader)
                .retrieve()
                .body(List.class);
    }
}