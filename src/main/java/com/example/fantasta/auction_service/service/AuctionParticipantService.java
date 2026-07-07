package com.example.fantasta.auction_service.service;
import com.example.fantasta.auction_service.repository.AuctionParticipantRepository;
import com.example.fantasta.auction_service.entity.AuctionParticipant;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuctionParticipantService 
{
    private final AuctionParticipantRepository auctionParticipantRepository;

    public AuctionParticipantService(AuctionParticipantRepository auctionParticipantRepository) 
    {
        this.auctionParticipantRepository = auctionParticipantRepository;
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
        auctionParticipantRepository.findByAuctionId(auctionId);
    }
}
