package com.example.fantasta.auction_service.service;

import com.example.fantasta.auction_service.client.AuthServiceClient;
import com.example.fantasta.auction_service.dto.AuctionResponse;
import com.example.fantasta.auction_service.dto.AuthUserResponse;
import com.example.fantasta.auction_service.dto.CreateAuctionRequest;
import com.example.fantasta.auction_service.entity.Auction;
import com.example.fantasta.auction_service.repository.AuctionRepository;
import com.example.fantasta.auction_service.enumeration.AuctionStatus;
import com.example.fantasta.auction_service.exception.CreationException;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final AuthServiceClient authServiceClient;

    public AuctionService(AuctionRepository auctionRepository, AuthServiceClient authServiceClient) {
        this.auctionRepository = auctionRepository;
        this.authServiceClient = authServiceClient;
    }

    public AuctionResponse createAuction(String authorizationHeader, CreateAuctionRequest request) throws TokenException, CreationException {
        
    	AuthUserResponse user = authServiceClient.getAuthenticatedUser(authorizationHeader);

        validateCreateAuctionRequest(request);

        Auction auction = new Auction();
        auction.setAuctionCode(generateAuctionCode());
        auction.setName(request.getName());
        auction.setMaxPlayersPerTeam(request.getMaxPlayersPerTeam());
        auction.setMaxGoalkeepers(request.getMaxGoalkeepers());
        auction.setMaxDefenders(request.getMaxDefenders());
        auction.setMaxMidfielders(request.getMaxMidfielders());
        auction.setMaxForwards(request.getMaxForwards());
        auction.setInitialCredits(request.getInitialCredits());
        auction.setCreatorUserId(user.getId());
        auction.setStatus(AuctionStatus.CREATED);

        Auction savedAuction = auctionRepository.save(auction);

        return mapToResponse(savedAuction);
    }

    private void validateCreateAuctionRequest(CreateAuctionRequest request) throws CreationException
    {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new CreationException("Auction name is required");
        }

        if (request.getMaxPlayersPerTeam() <= 0) {
            throw new CreationException("maxPlayersPerTeam must be greater than 0");
        }

        if (request.getMaxGoalkeepers() <= 0 ||
            request.getMaxDefenders() <= 0 ||
            request.getMaxMidfielders() <= 0 ||
            request.getMaxForwards() <= 0) {
            throw new CreationException("All role limits must be greater than 0");
        }

        if (request.getInitialCredits() <= 0) {
            throw new CreationException("initialCredits must be greater than 0");
        }

        int totalRoles = request.getMaxGoalkeepers()
                + request.getMaxDefenders()
                + request.getMaxMidfielders()
                + request.getMaxForwards();

        if (totalRoles != request.getMaxPlayersPerTeam()) {
            throw new CreationException("The sum of role limits must equal maxPlayersPerTeam");
        }
    }

    private String generateAuctionCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private AuctionResponse mapToResponse(Auction auction) {
        AuctionResponse response = new AuctionResponse();
        response.setId(auction.getId());
        response.setAuctionCode(auction.getAuctionCode());
        response.setName(auction.getName());
        response.setMaxPlayersPerTeam(auction.getMaxPlayersPerTeam());
        response.setMaxGoalkeepers(auction.getMaxGoalkeepers());
        response.setMaxDefenders(auction.getMaxDefenders());
        response.setMaxMidfielders(auction.getMaxMidfielders());
        response.setMaxForwards(auction.getMaxForwards());
        response.setInitialCredits(auction.getInitialCredits());
        response.setCreatorUserId(auction.getCreatorUserId());
        response.setStatus(auction.getStatus());
        return response;
    }
    
    public AuctionResponse getAuctionById(String authorizationHeader, int auctionId) throws NotFoundException, TokenException
    {
        AuthUserResponse res;
        try
        {
             res = authServiceClient.getAuthenticatedUser(authorizationHeader); // Validate token
        }
        catch(TokenException e)
        {
            throw new TokenException("Invalid or expired token");
        }

        if(res == null)
        {
            throw new TokenException("Invalid or expired token");
        }

        Optional<Auction> auctionOptional = auctionRepository.findById(auctionId);

        if (!auctionOptional.isPresent()) {
            throw new NotFoundException("Auction with ID " + auctionId + " not found");
        }

        Auction auction = auctionOptional.get();
        return mapToResponse(auction);
    }

    public boolean isOwner(int auctionId, Long uID) throws NotFoundException
    {
        Optional<Auction> auctionOptional = auctionRepository.findById(auctionId);

        if (!auctionOptional.isPresent()) {
            throw new NotFoundException("Auction with ID " + auctionId + " not found");
        }

        Auction auction = auctionOptional.get();

        return auction.getCreatorUserId().equals(uID);
    }
    
}