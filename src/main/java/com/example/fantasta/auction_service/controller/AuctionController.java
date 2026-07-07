/*
    Scritto da Cervera Francesco
    (C) Diritti Riservati
*/

package com.example.fantasta.auction_service.controller;

import com.example.fantasta.auction_service.dto.AuctionResponse;
import com.example.fantasta.auction_service.dto.AuthUserResponse;
import com.example.fantasta.auction_service.dto.CreateAuctionRequest;
import com.example.fantasta.auction_service.dto.UpdateAuctionRequest;
import com.example.fantasta.auction_service.entity.Auction;
import com.example.fantasta.auction_service.enumeration.AuctionStatus;
import com.example.fantasta.auction_service.exception.CreationException;
import com.example.fantasta.auction_service.exception.ForbiddenException;
import com.example.fantasta.auction_service.exception.InvalidStateException;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.service.AuctionService;
import com.example.fantasta.auction_service.service.AuctionParticipantService;
import com.example.fantasta.auction_service.client.AuthServiceClient;
import com.example.fantasta.auction_service.dto.UpdateStatusRequest;
import com.example.fantasta.auction_service.enumeration.AuctionStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;
    private final AuctionParticipantService auctionParticipantService;
    private final AuthServiceClient authServiceClient;
    public AuctionController(AuctionService auctionService, AuctionParticipantService auctionParticipantService, AuthServiceClient authServiceClient) {
        this.auctionService = auctionService;
        this.auctionParticipantService = auctionParticipantService;
        this.authServiceClient = authServiceClient;
    }

    /*
        Metodo che consete la creazione di un'asta. 
        @param authorizationHeader: Header di autorizzazione contenente il token dell'utente autenticato.
        @param request: Oggetto contenente i dati necessari per creare l'asta.
        @return ResponseEntity contenente la risposta dell'asta creata.
        @throws TokenException: Se il token di autorizzazione non è valido o scaduto. 
        @throws CreationException: Se si verifica un errore durante la creazione dell'asta.
    */
    @PostMapping("/create")
    public ResponseEntity<AuctionResponse> createAuction(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CreateAuctionRequest request) throws TokenException, CreationException {

        AuctionResponse response = auctionService.createAuction(authorizationHeader, request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /*
        Metodo che consente di ottenere un'asta per ID.
        @param authorizationHeader: Header di autorizzazione contenente il token dell'utente autenticato.
        @param auctionId: ID dell'asta da recuperare.
        @return ResponseEntity contenente la risposta dell'asta recuperata.
        @throws NotFoundException: Se l'asta non è stata trovata.
        @throws TokenException: Se il token di autorizzazione non è valido o scaduto.

    */
    @GetMapping("/{auctionId}")
    public ResponseEntity<AuctionResponse> getAuctionById(
        @RequestHeader("Authorization") String authorizationHeader,
        @PathVariable int auctionId)
    {

        try
        {
            AuctionResponse respone = auctionService.getAuctionById(authorizationHeader, auctionId);
            return ResponseEntity.ok(respone);
        }
        catch(NotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch(TokenException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @PatchMapping("/{auctionId}/status")
    public ResponseEntity<?> updateAuctionStatus(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int auctionId,
            @RequestBody UpdateStatusRequest request) 
    {

        try {
            AuctionResponse response = auctionService.updateStatus(authorizationHeader, auctionId, AuctionStatus.fromString(request.getStatus()));
            return ResponseEntity.ok(response);

        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PutMapping("/{auctionId}")
    public ResponseEntity<?> updateAuction(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int auctionId,
            @RequestBody UpdateAuctionRequest request) {

        try {
            AuctionResponse response = auctionService.updateAuction(
                    authorizationHeader,
                    auctionId,
                    request.getName(),
                    request.getMaxPlayersPerTeam(),
                    request.getMaxGoalkeepers(),
                    request.getMaxDefenders(),
                    request.getMaxMidfielders(),
                    request.getMaxForwards(),
                    request.getInitialCredits()
            );
            return ResponseEntity.ok(response);
        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (InvalidStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{auctionId}")
    public ResponseEntity<?> deleteAuction(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int auctionId) {

        try {
            auctionService.deleteAuction(authorizationHeader, auctionId);
            return ResponseEntity.ok("Auction deleted successfully");
        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}