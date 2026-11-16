package com.example.fantasta.auction_service.repository;

import com.example.fantasta.auction_service.entity.FantasyTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;

import java.util.List;

public interface FantasyTeamRepository extends JpaRepository<FantasyTeam, Integer> {

    List<FantasyTeam> findByAuctionId(int auctionId);

    Optional<FantasyTeam> findByAuctionIdAndId(int auctionId, int id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<FantasyTeam> findWithLockByAuctionIdAndId(int auctionId, int id);
     
}
