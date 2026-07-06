package com.example.fantasta.auction_service.service;
import com.example.fantasta.auction_service.repository.AuctionParticipantRepository;
import com.example.fantasta.auction_service.entity.AuctionParticipant;
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
}
