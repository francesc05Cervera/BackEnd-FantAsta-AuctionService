package com.example.fantasta.auction_service.repository;

import com.example.fantasta.auction_service.entity.AuctionParticipant;

import java.time.LocalDateTime;

import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionParticipantRepository extends JpaRepository<AuctionParticipant, Integer> 
{

    boolean existsByAuctionIdAndUserId(int auctionId, Long userId);

    void deleteByJoinedAtBefore(LocalDateTime cutoff);

    List<AuctionParticipant> findByAuctionId(int auctionId);

    Optional<AuctionParticipant> findByAuctionIdAndUserId(int auctionId, Long userId);
}