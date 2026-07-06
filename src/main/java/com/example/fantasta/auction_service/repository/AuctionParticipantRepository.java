package com.example.fantasta.auction_service.repository;

import com.example.fantasta.auction_service.entity.AuctionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionParticipantRepository extends JpaRepository<AuctionParticipant, Integer> 
{

    boolean existsByAuctionIdAndUserId(int auctionId, Long userId);
}