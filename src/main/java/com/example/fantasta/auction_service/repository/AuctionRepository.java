package com.example.fantasta.auction_service.repository;

import com.example.fantasta.auction_service.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {

    boolean existsByAuctionCode(String auctionCode);

    Auction findByAuctionCode(String auctionCode);

    Auction findById(int id);
}