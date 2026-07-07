package com.example.fantasta.auction_service.service;

import com.example.fantasta.auction_service.dto.AssignPlayerRequest;
import com.example.fantasta.auction_service.dto.UpdateAssignmentRequest;
import com.example.fantasta.auction_service.entity.PlayerAssignment;
import com.example.fantasta.auction_service.repository.PlayerAssignmentRepository;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.exception.ForbiddenException;
import com.example.fantasta.auction_service.exception.BudgetExceededException;
import com.example.fantasta.auction_service.exception.LimitExceededException;
import com.example.fantasta.auction_service.exception.DuplicateAssignmentException;

import com.example.fantasta.auction_service.client.AuthServiceClient;
import com.example.fantasta.auction_service.dto.AuthUserResponse;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerAssignmentService {

    private final PlayerAssignmentRepository playerAssignmentRepository;
    private final AuthServiceClient authServiceClient;
    private final AuctionService auctionService;
    private final FantasyTeamService fantasyTeamService;

    public PlayerAssignmentService(PlayerAssignmentRepository playerAssignmentRepository,
                                   AuthServiceClient authServiceClient,
                                   AuctionService auctionService,
                                   FantasyTeamService fantasyTeamService) {
        this.playerAssignmentRepository = playerAssignmentRepository;
        this.authServiceClient = authServiceClient;
        this.auctionService = auctionService;
        this.fantasyTeamService = fantasyTeamService;
    }

    private AuthUserResponse validateToken(String authorizationHeader) throws TokenException {
        return authServiceClient.getAuthenticatedUser(authorizationHeader);
    }

    // 1. Assegnare giocatore
    public PlayerAssignment assignPlayer(
            String authorizationHeader,
            Integer auctionId,
            Integer teamId,
            Integer playerId,
            String role,
            Integer cost)
            throws TokenException, NotFoundException, ForbiddenException,
                   BudgetExceededException, LimitExceededException, DuplicateAssignmentException {

        validateToken(authorizationHeader);

        // 2. Verifica asta
        try {
            auctionService.getAuctionById(authorizationHeader, auctionId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Auction with ID " + auctionId + " not found");
        } catch (TokenException e) {
            throw e;
        }

        // 3. Verifica team (esiste per questa asta e appartiene all’asta)
        // Nota: non esiste ancora un metodo diretto, ma puoi usare listTeams o aggiungere existsByAuctionIdAndTeamId
        // Per ora usiamo una logica semplice:
        List<com.example.fantasta.auction_service.entity.FantasyTeam> teams =
                fantasyTeamService.listTeams(authorizationHeader, auctionId);
        boolean teamExists = false;
        for (com.example.fantasta.auction_service.entity.FantasyTeam t : teams) {
            if (t.getId() == teamId) {
                teamExists = true;
                break;
            }
        }
        if (!teamExists) {
            throw new NotFoundException("Team with ID " + teamId + " not found for this auction");
        }

        // 4. Verifica duplicato: giocatore già assegnato in questa asta
        List<PlayerAssignment> existing = playerAssignmentRepository.findByAuctionId(auctionId);
        for (PlayerAssignment pa : existing) {
            if (pa.getPlayerId() == playerId) {
                throw new DuplicateAssignmentException("Player already assigned in this auction");
            }
        }

        // 5. Verifica limiti per ruolo nell’asta
        int maxForRole = getMaxPlayersByRole(auctionId, role);
        int currentForRole = 0;
        for (PlayerAssignment pa : existing) {
            if (pa.getRole() != null && pa.getRole().equalsIgnoreCase(role)) {
                currentForRole++;
            }
        }
        if (currentForRole >= maxForRole) {
            throw new LimitExceededException("Limit exceeded for role: " + role);
        }

        // 6. Verifica budget del team
        int currentCost = getCurrentCostForTeam(auctionId, teamId);
        int newTotal = currentCost + cost;
        int budget = getBudgetForTeam(auctionId, teamId);
        if (newTotal > budget) {
            throw new BudgetExceededException("Budget exceeded for this team");
        }

        PlayerAssignment assignment = new PlayerAssignment();
        assignment.setAuctionId(auctionId);
        assignment.setTeamId(teamId);
        assignment.setPlayerId(playerId);
        assignment.setRole(role);
        assignment.setCost(cost);

        return playerAssignmentRepository.save(assignment);
    }

    // Helper: maxPlayers per ruolo nell’asta
    private int getMaxPlayersByRole(Integer auctionId, String role) {
        // Usa AuctionStatus per decidere quale campo usare
        // Per semplicità, assumiamo che i limiti siano fissi in Auction
        try {
            com.example.fantasta.auction_service.dto.AuctionResponse auction =
                    auctionService.getAuctionById(null, auctionId); // senza auth se vuoi, oppure passa header
            if (role == null) {
                return 0;
            }
            switch (role.toUpperCase()) {
                case "PORTIERE":
                    return auction.getMaxGoalkeepers();
                case "DIFENSORE":
                    return auction.getMaxDefenders();
                case "CENTROCAMPISTA":
                    return auction.getMaxMidfielders();
                case "ATTACCANTE":
                    return auction.getMaxForwards();
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    // Helper: budget del team
    private int getBudgetForTeam(Integer auctionId, Integer teamId) {
        try {
            List<com.example.fantasta.auction_service.entity.FantasyTeam> teams =
                    fantasyTeamService.listTeams(null, auctionId);
            for (com.example.fantasta.auction_service.entity.FantasyTeam t : teams) {
                if (t.getId() == teamId) {
                    return t.getBudget();
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    // Helper: costo attuale del team
    private int getCurrentCostForTeam(Integer auctionId, Integer teamId) {
        List<PlayerAssignment> assignments = playerAssignmentRepository.findByAuctionIdAndTeamId(auctionId, teamId);
        int total = 0;
        for (PlayerAssignment pa : assignments) {
            total += pa.getCost();
        }
        return total;
    }

    // 2. Listare assegnazioni per asta
    public List<PlayerAssignment> listAssignmentsByAuction(Integer auctionId) {
        return playerAssignmentRepository.findByAuctionId(auctionId);
    }

    // 3. Listare assegnazioni per asta + team
    public List<PlayerAssignment> listAssignmentsByAuctionAndTeam(Integer auctionId, Integer teamId) {
        return playerAssignmentRepository.findByAuctionIdAndTeamId(auctionId, teamId);
    }

    // 4. Update ruolo/costo di un’assegnazione
    public PlayerAssignment updateAssignmentRoleCost(
            Integer assignmentId,
            String newRole,
            Integer newCost)
            throws NotFoundException {

        PlayerAssignment assignment = playerAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new NotFoundException("Assignment not found"));

        assignment.setRole(newRole);
        assignment.setCost(newCost);

        return playerAssignmentRepository.save(assignment);
    }
}