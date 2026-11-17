package com.example.fantasta.auction_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fantasta.auction_service.client.AuthServiceClient;
import com.example.fantasta.auction_service.client.PlayerServiceClient;
import com.example.fantasta.auction_service.dto.AuthUserResponse;
import com.example.fantasta.auction_service.dto.PlayerDTO;
import com.example.fantasta.auction_service.entity.Auction;
import com.example.fantasta.auction_service.entity.FantasyTeam;
import com.example.fantasta.auction_service.entity.PlayerAssignment;
import com.example.fantasta.auction_service.enumeration.AuctionStatus;
import com.example.fantasta.auction_service.exception.BudgetExceededException;
import com.example.fantasta.auction_service.exception.DuplicateAssignmentException;
import com.example.fantasta.auction_service.exception.ForbiddenException;
import com.example.fantasta.auction_service.exception.LimitExceededException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.repository.AuctionParticipantRepository;
import com.example.fantasta.auction_service.repository.AuctionRepository;
import com.example.fantasta.auction_service.repository.FantasyTeamRepository;
import com.example.fantasta.auction_service.repository.PlayerAssignmentRepository;

@Service
public class PlayerAssignmentService {
    private final PlayerAssignmentRepository assignments;
    private final AuctionRepository auctions;
    private final FantasyTeamRepository teams;
    private final AuctionParticipantRepository participants;
    private final AuthServiceClient auth;
    private final PlayerServiceClient players;

    public PlayerAssignmentService(PlayerAssignmentRepository assignments, AuctionRepository auctions,
            FantasyTeamRepository teams, AuctionParticipantRepository participants, AuthServiceClient auth,
            PlayerServiceClient players) {
        this.assignments = assignments; this.auctions = auctions; this.teams = teams;
        this.participants = participants; this.auth = auth; this.players = players;
    }

    @Transactional(rollbackFor = Exception.class)
    public PlayerAssignment assignPlayer(String header, Integer auctionId, Integer teamId, Integer playerId, String role, Integer cost)
            throws TokenException, NotFoundException, ForbiddenException, BudgetExceededException, LimitExceededException, DuplicateAssignmentException {
        AuthUserResponse user = auth.getAuthenticatedUser(header);
        Auction auction = lockAuction(auctionId);
        requireOwner(auction, user);
        requireOpen(auction);
        if (teamId == null || playerId == null || cost == null || cost < 0) throw new LimitExceededException("Assignment data is invalid");
        FantasyTeam team = teams.findWithLockByAuctionIdAndId(auctionId, teamId)
                .orElseThrow(() -> new NotFoundException("Team not found for this auction"));
        PlayerDTO player = players.getPlayer(header, playerId);
        if (role == null || !role.equalsIgnoreCase(player.getRole())) throw new LimitExceededException("Assignment role must match the player role");
        List<PlayerAssignment> current = assignments.findByAuctionIdAndTeamId(auctionId, teamId);
        if (assignments.existsByAuctionIdAndPlayerId(auctionId, playerId)) throw new DuplicateAssignmentException("Player already assigned in this auction");
        enforceRoster(auction, team, current, player.getRole(), cost, null);
        return assignments.save(new PlayerAssignment(auctionId, teamId, playerId, player.getRole().toUpperCase(), cost));
    }

    @Transactional(rollbackFor = Exception.class)
    public PlayerAssignment updateAssignmentRoleCost(String header, Integer assignmentId, String role, Integer cost)
            throws TokenException, NotFoundException, ForbiddenException, BudgetExceededException, LimitExceededException {
        AuthUserResponse user = auth.getAuthenticatedUser(header);
        PlayerAssignment assignment = assignments.findById(assignmentId).orElseThrow(() -> new NotFoundException("Assignment not found"));
        Auction auction = lockAuction(assignment.getAuctionId());
        requireOwner(auction, user); requireOpen(auction);
        if (cost == null || cost < 0) throw new LimitExceededException("Cost must be non-negative");
        FantasyTeam team = teams.findWithLockByAuctionIdAndId(auction.getId(), assignment.getTeamId())
                .orElseThrow(() -> new NotFoundException("Team not found for this auction"));
        PlayerDTO player = players.getPlayer(header, assignment.getPlayerId());
        if (role == null || !role.equalsIgnoreCase(player.getRole())) throw new LimitExceededException("Assignment role must match the player role");
        enforceRoster(auction, team, assignments.findByAuctionIdAndTeamId(auction.getId(), team.getId()), player.getRole(), cost, assignment.getId());
        assignment.setRole(player.getRole().toUpperCase()); assignment.setCost(cost);
        return assignments.save(assignment);
    }

    public List<PlayerAssignment> listAssignmentsByAuction(String header, Integer auctionId) throws TokenException, ForbiddenException {
        requireParticipantOrOwner(auctionId, auth.getAuthenticatedUser(header)); return assignments.findByAuctionId(auctionId);
    }

    public List<PlayerAssignment> listAssignmentsByAuctionAndTeam(String header, Integer auctionId, Integer teamId) throws TokenException, ForbiddenException {
        requireParticipantOrOwner(auctionId, auth.getAuthenticatedUser(header)); return assignments.findByAuctionIdAndTeamId(auctionId, teamId);
    }

    public List<PlayerDTO> getAvailablePlayers(String header, Integer auctionId) throws TokenException, ForbiddenException {
        requireParticipantOrOwner(auctionId, auth.getAuthenticatedUser(header));
        List<Integer> assigned = assignments.findPlayerIdsByAuctionId(auctionId);
        return players.getPlayers(header).stream().filter(player -> !assigned.contains(player.getId())).toList();
    }

    private Auction lockAuction(Integer auctionId) {
        return auctions.findWithLockById(auctionId).orElseThrow(() -> new NotFoundException("Auction not found"));
    }

    private void requireOwner(Auction auction, AuthUserResponse user) throws ForbiddenException {
        if (!auction.getCreatorUserId().equals(user.getId())) throw new ForbiddenException("Only the auction owner can manage assignments");
    }

    private void requireOpen(Auction auction) throws LimitExceededException {
        if (auction.getStatus() != AuctionStatus.OPEN) throw new LimitExceededException("Assignments are allowed only when the auction is open");
    }

    private void requireParticipantOrOwner(Integer auctionId, AuthUserResponse user) throws ForbiddenException {
        Auction auction = auctions.findById(auctionId).orElseThrow(() -> new NotFoundException("Auction not found"));
        boolean permitted = auction.getCreatorUserId().equals(user.getId()) || participants.findByAuctionIdAndUserId(auctionId, user.getId()).map(p -> p.isApproved()).orElse(false);
        if (!permitted) throw new ForbiddenException("You are not an approved participant of this auction");
    }

    private void enforceRoster(Auction auction, FantasyTeam team, List<PlayerAssignment> current, String role, int proposedCost, Integer ignoredAssignment)
            throws BudgetExceededException, LimitExceededException {
        List<PlayerAssignment> effective = current.stream().filter(item -> !item.getId().equals(ignoredAssignment)).toList();
        if (effective.size() >= auction.getMaxPlayersPerTeam()) throw new LimitExceededException("Team roster limit exceeded");
        long roleCount = effective.stream().filter(item -> item.getRole().equalsIgnoreCase(role)).count();
        int roleLimit = switch (role.toUpperCase()) {
            case "PORTIERE" -> auction.getMaxGoalkeepers(); case "DIFENSORE" -> auction.getMaxDefenders();
            case "CENTROCAMPISTA" -> auction.getMaxMidfielders(); case "ATTACCANTE" -> auction.getMaxForwards();
            default -> throw new LimitExceededException("Unknown player role");
        };
        if (roleCount >= roleLimit) throw new LimitExceededException("Role limit exceeded");
        int spent = effective.stream().mapToInt(PlayerAssignment::getCost).sum();
        if (spent + proposedCost > team.getBudget()) throw new BudgetExceededException("Team budget exceeded");
    }
}
