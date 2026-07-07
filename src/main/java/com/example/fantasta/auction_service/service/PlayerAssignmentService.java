package com.example.fantasta.auction_service.service;

import com.example.fantasta.auction_service.entity.Auction;
import com.example.fantasta.auction_service.entity.FantasyTeam;
import com.example.fantasta.auction_service.entity.PlayerAssignment;
import com.example.fantasta.auction_service.repository.AuctionRepository;
import com.example.fantasta.auction_service.repository.FantasyTeamRepository;
import com.example.fantasta.auction_service.repository.PlayerAssignmentRepository;
import com.example.fantasta.auction_service.client.AuthServiceClient;
import com.example.fantasta.auction_service.dto.AuthUserResponse;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.exception.ForbiddenException;
import com.example.fantasta.auction_service.exception.BudgetExceededException;
import com.example.fantasta.auction_service.exception.LimitExceededException;
import com.example.fantasta.auction_service.exception.DuplicateAssignmentException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerAssignmentService {

    private final AuctionRepository auctionRepository;
    private final FantasyTeamRepository fantasyTeamRepository;
    private final PlayerAssignmentRepository playerAssignmentRepository;
    private final AuthServiceClient authServiceClient;

    public PlayerAssignmentService(AuctionRepository auctionRepository,
                                   FantasyTeamRepository fantasyTeamRepository,
                                   PlayerAssignmentRepository playerAssignmentRepository,
                                   AuthServiceClient authServiceClient) {
        this.auctionRepository = auctionRepository;
        this.fantasyTeamRepository = fantasyTeamRepository;
        this.playerAssignmentRepository = playerAssignmentRepository;
        this.authServiceClient = authServiceClient;
    }

public PlayerAssignment assignPlayer(
        String authorizationHeader,
        int auctionId,
        int teamId,
        int playerId,
        String role,
        int cost)
        throws TokenException, NotFoundException, ForbiddenException,
               BudgetExceededException, LimitExceededException, DuplicateAssignmentException {

    AuthUserResponse user = authServiceClient.getAuthenticatedUser(authorizationHeader);

    if (user == null) {
        throw new TokenException("Invalid or expired token");
    }

    Auction auction = auctionRepository.findById(auctionId)
            .orElseThrow(() -> new NotFoundException("Auction with ID " + auctionId + " not found"));

    if (!auction.getCreatorUserId().equals(user.getId())) {
        throw new ForbiddenException("Only the auction owner can assign players.");
    }

    FantasyTeam team = fantasyTeamRepository.findById(teamId)
            .orElseThrow(() -> new NotFoundException("Team with ID " + teamId + " not found."));

    if (team.getAuctionId() != auctionId) {
        throw new ForbiddenException("Team does not belong to this auction.");
    }

    if (team.getOwnerUserId().equals(user.getId())) {
        throw new ForbiddenException("You cannot assign players to your own team.");
    }

    if (playerAssignmentRepository.findByAuctionId(auctionId)
            .stream()
            .anyMatch(p -> p.getPlayerId() == playerId)) {
        throw new DuplicateAssignmentException("Player is already assigned in this auction.");
    }

    int currentCount = playerAssignmentRepository.countByTeamIdAndRole(teamId, role);
    int max = getMaxForRole(team, role);
    if (currentCount >= max) {
        throw new LimitExceededException("Maximum number of " + role + " for this team reached.");
    }

    if (team.getBudget() < cost) {
        throw new BudgetExceededException("Team budget is not sufficient for this player.");
    }

    team.setBudget(team.getBudget() - cost);
    fantasyTeamRepository.save(team);

    PlayerAssignment assignment = new PlayerAssignment();
    assignment.setAuctionId(auctionId);
    assignment.setTeamId(teamId);
    assignment.setPlayerId(playerId);
    assignment.setRole(role);
    assignment.setCost(cost);
    assignment.setOwnerUserId(user.getId());
    assignment.setAssignedAt(java.time.LocalDateTime.now());

    return playerAssignmentRepository.save(assignment);
}

private int getMaxForRole(FantasyTeam team, String role) {
    switch (role) {
        case "PORTIERE":
            return team.getMaxGoalkeepers();
        case "DIFENSORE":
            return team.getMaxDefenders();
        case "CENTROCAMPISTA":
            return team.getMaxMidfielders();
        case "ATTACCANTE":
            return team.getMaxForwards();
        default:
            return 0;
    }
}

    public List<PlayerAssignment> listAssignments(String authorizationHeader, int auctionId)
            throws TokenException, NotFoundException, ForbiddenException {

        AuthUserResponse user = authServiceClient.getAuthenticatedUser(authorizationHeader);

        if (user == null) {
            throw new TokenException("Invalid or expired token");
        }

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new NotFoundException("Auction with ID " + auctionId + " not found."));

        if (!auction.getCreatorUserId().equals(user.getId())) {
            throw new ForbiddenException("Only the auction owner can list assignments.");
        }

        return playerAssignmentRepository.findByAuctionId(auctionId);
    }
}