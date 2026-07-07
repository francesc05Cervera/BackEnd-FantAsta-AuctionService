package com.example.fantasta.auction_service.repository;

import com.example.fantasta.auction_service.entity.PlayerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerAssignmentRepository extends JpaRepository<PlayerAssignment, Integer> {

    List<PlayerAssignment> findByAuctionId(Integer auctionId);

    List<PlayerAssignment> findByAuctionIdAndTeamId(Integer auctionId, Integer teamId);
}