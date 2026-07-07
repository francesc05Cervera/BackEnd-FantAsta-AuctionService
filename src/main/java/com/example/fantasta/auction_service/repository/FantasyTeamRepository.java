package com.example.fantasta.auction_service.repository;

import com.example.fantasta.auction_service.entity.FantasyTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FantasyTeamRepository extends JpaRepository<FantasyTeam, Integer> {

    List<FantasyTeam> findByAuctionId(int auctionId);
     
}