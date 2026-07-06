package com.example.fantasta.auction_service.controller;

import com.example.fantasta.auction_service.dto.AuctionResponse;
import com.example.fantasta.auction_service.dto.CreateAuctionRequest;
import com.example.fantasta.auction_service.exception.CreationException;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.service.AuctionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("/create")
    public ResponseEntity<AuctionResponse> createAuction(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CreateAuctionRequest request) throws TokenException, CreationException {

        AuctionResponse response = auctionService.createAuction(authorizationHeader, request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{auctionId}")
    public ResponseEntity<AuctionResponse> getAuctionById(@PathVariable int auctionId)
    {
        try
        {
            AuctionResponse respone = auctionService.getAuctionById(auctionId);
            return ResponseEntity.ok(respone);
        }
        catch(NotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}