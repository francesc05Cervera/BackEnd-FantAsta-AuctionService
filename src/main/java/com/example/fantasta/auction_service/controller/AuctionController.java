package com.example.fantasta.auction_service.controller;

import com.example.fantasta.auction_service.dto.AuctionResponse;
import com.example.fantasta.auction_service.dto.AuthUserResponse;
import com.example.fantasta.auction_service.dto.CreateAuctionRequest;
import com.example.fantasta.auction_service.enumeration.AuctionStatus;
import com.example.fantasta.auction_service.exception.CreationException;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.service.AuctionService;
import com.example.fantasta.auction_service.service.AuctionParticipantService;
import com.example.fantasta.auction_service.client.AuthServiceClient;
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

    @PostMapping("/create")
    public ResponseEntity<AuctionResponse> createAuction(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CreateAuctionRequest request) throws TokenException, CreationException {

        AuctionResponse response = auctionService.createAuction(authorizationHeader, request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
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

    @GetMapping("/{auctionId}/participants")
    public ResponseEntity<?> getParticipants(
        @RequestHeader("Authorization") String authorizationHeader,
        @PathVariable int auctionId) 
    {
        try
        {
            AuthUserResponse user = authServiceClient.getAuthenticatedUser(authorizationHeader);
            AuctionResponse auction = auctionService.getAuctionById(authorizationHeader, auctionId);

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