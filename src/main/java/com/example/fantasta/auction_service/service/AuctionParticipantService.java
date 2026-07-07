package com.example.fantasta.auction_service.service;
import com.example.fantasta.auction_service.repository.AuctionParticipantRepository;
import com.example.fantasta.auction_service.dto.AuthUserResponse;
import com.example.fantasta.auction_service.entity.Auction;
import com.example.fantasta.auction_service.entity.AuctionParticipant;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.exception.ForbiddenException;
import com.example.fantasta.auction_service.repository.AuctionRepository;
import com.example.fantasta.auction_service.client.AuthServiceClient;


import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuctionParticipantService 
{
    private final AuctionParticipantRepository auctionParticipantRepository;
    private final AuctionRepository auctionRepository; 
    private final AuthServiceClient authServiceClient;
    public AuctionParticipantService(AuctionParticipantRepository auctionParticipantRepository, AuctionRepository auctionRepository, AuthServiceClient authServiceClient) 
    {
        this.auctionParticipantRepository = auctionParticipantRepository;
        this.auctionRepository = auctionRepository;
        this.authServiceClient = authServiceClient;
    }
   
    
    private boolean isUserAlreadyJoined(int auctionId, Long userId) 
    {
        return auctionParticipantRepository.existsByAuctionIdAndUserId(auctionId, userId);
    }

    public boolean join(int auctionId, Long userId) 
    {
        if (isUserAlreadyJoined(auctionId, userId)) 
        {
            return false; 
        }

        AuctionParticipant participant = new AuctionParticipant();
        participant.setAuctionId(auctionId);   
        participant.setUserId(userId);
        participant.setJoinedAt(java.time.LocalDateTime.now());
        auctionParticipantRepository.save(participant);
        return true;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupOldParticipants() 
    {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(5);
        auctionParticipantRepository.deleteByJoinedAtBefore(cutoff);
    }

    public List<AuctionParticipant> getParticipant(int auctionId) 
    {
        return auctionParticipantRepository.findByAuctionId(auctionId);
    }

    public void approveParticipant(String authorizationHeader, int auctionId, Long participantUserId)
        throws TokenException, NotFoundException, ForbiddenException {

    AuthUserResponse user = authServiceClient.getAuthenticatedUser(authorizationHeader);

    if (user == null) {
        throw new TokenException("Invalid or expired token");
    }

    Auction auction = auctionRepository.findById(auctionId)
            .orElseThrow(() -> new NotFoundException("Auction with ID " + auctionId + " not found"));

    if (!auction.getCreatorUserId().equals(user.getId())) {
        throw new ForbiddenException("Only the auction owner can approve participants.");
    }

    AuctionParticipant participant = auctionParticipantRepository
            .findByAuctionIdAndUserId(auctionId, participantUserId)
            .orElseThrow(() -> new NotFoundException("Participant not found for this auction"));

    participant.setApproved(true);
    auctionParticipantRepository.save(participant);
}
}
