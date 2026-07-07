package com.example.fantasta.auction_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fantasta.auction_service.client.AuthServiceClient;
import com.example.fantasta.auction_service.dto.AuctionResponse;
import com.example.fantasta.auction_service.dto.AuthUserResponse;
import com.example.fantasta.auction_service.enumeration.AuctionStatus;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.service.AuctionParticipantService;
import com.example.fantasta.auction_service.service.AuctionService;

@RestController
@RequestMapping("api/participants")
public class AuctionParticipantController 
{
    private final AuctionService auctionService;
    private final AuthServiceClient authServiceClient;
    private final AuctionParticipantService auctionParticipantService;

    public AuctionParticipantController(AuctionService auctionService,
                                        AuthServiceClient authServiceClient,
                                        AuctionParticipantService auctionParticipantService) {
        this.auctionService = auctionService;
        this.authServiceClient = authServiceClient;
        this.auctionParticipantService = auctionParticipantService;
    }
    
     /*
        Metodo che consente a un utente di unirsi a un'asta.
        @param authorizationHeader: Header di autorizzazione contenente il token dell'utente autenticato.
        @param auctionId: ID dell'asta a cui l'utente desidera unirsi.
        @return ResponseEntity contenente il risultato dell'operazione di join.
        @throws TokenException: Se il token di autorizzazione non è valido o scaduto.
        @throws NotFoundException: Se l'asta non è stata trovata.

    */
    @PostMapping("/{auctionId}/join")
    public ResponseEntity<?> join(
        @RequestHeader("Authorization") String authorizationHeader,
        @PathVariable int auctionId) 
    {
        

        try
        {
            AuctionResponse res = auctionService.getAuctionById(authorizationHeader, auctionId);
            AuthUserResponse user = authServiceClient.getAuthenticatedUser(authorizationHeader);
            
            if(res.getStatus() != AuctionStatus.OPEN)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Auction is not open for joining.");
            }

            if(auctionParticipantService.join(auctionId, user.getId()))
            {
                return ResponseEntity.ok("User joined the auction successfully.");
            } 
            else 
            {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User has already joined the auction.");
            }
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /*
        Metodo che consente di ottenere i partecipanti a un'asta.
        @param authorizationHeader: Header di autorizzazione contenente il token dell'utente autenticato.
        @param auctionId: ID dell'asta per cui recuperare i partecipanti.
        @return ResponseEntity contenente la lista dei partecipanti.
        @throws NotFoundException: Se l'asta non è stata trovata.
        @throws TokenException: Se il token di autorizzazione non è valido o scaduto.
    */

    @GetMapping("/{auctionId}/participants")
    public ResponseEntity<?> getParticipants(
        @RequestHeader("Authorization") String authorizationHeader,
        @PathVariable int auctionId) 
    {
        try
        {
            AuthUserResponse user = authServiceClient.getAuthenticatedUser(authorizationHeader);

            if (!auctionService.isOwner(auctionId, user.getId())) 
            {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the auction owner can view participants.");
            }
            return ResponseEntity.ok(auctionParticipantService.getParticipant(auctionId));
        }
        catch(NotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(TokenException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
