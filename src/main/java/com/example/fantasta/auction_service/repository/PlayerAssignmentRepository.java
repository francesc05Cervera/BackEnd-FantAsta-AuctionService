package com.example.fantasta.auction_service.repository;

import com.example.fantasta.auction_service.entity.PlayerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface PlayerAssignmentRepository extends JpaRepository<PlayerAssignment, Integer> {

    List<PlayerAssignment> findByAuctionId(Integer auctionId);

    List<PlayerAssignment> findByAuctionIdAndTeamId(Integer auctionId, Integer teamId);

    boolean existsByAuctionIdAndPlayerId(Integer auctionId, Integer playerId);

    @Query("select assignment.playerId from PlayerAssignment assignment where assignment.auctionId = :auctionId")
    List<Integer> findPlayerIdsByAuctionId(Integer auctionId);
}
