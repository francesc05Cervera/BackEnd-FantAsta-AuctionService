package com.example.fantasta.auction_service.repository;

import com.example.fantasta.auction_service.entity.PlayerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerAssignmentRepository extends JpaRepository<PlayerAssignment, Integer> {

    List<PlayerAssignment> findByAuctionId(int auctionId);

    List<PlayerAssignment> findByTeamId(int teamId);

    int countByTeamIdAndRole(int teamId, String role);
}