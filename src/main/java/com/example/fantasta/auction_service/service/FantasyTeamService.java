package com.example.fantasta.auction_service.service;

import com.example.fantasta.auction_service.entity.Auction;
import com.example.fantasta.auction_service.entity.FantasyTeam;
import com.example.fantasta.auction_service.entity.AuctionParticipant;
import com.example.fantasta.auction_service.repository.AuctionRepository;
import com.example.fantasta.auction_service.repository.AuctionParticipantRepository;
import com.example.fantasta.auction_service.repository.FantasyTeamRepository;
import com.example.fantasta.auction_service.dto.AuthUserResponse;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.exception.ForbiddenException;
import com.example.fantasta.auction_service.client.AuthServiceClient;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FantasyTeamService {

    private final AuctionRepository auctionRepository;
    private final AuctionParticipantRepository auctionParticipantRepository;
    private final FantasyTeamRepository fantasyTeamRepository;
    private final AuthServiceClient authServiceClient;

    public FantasyTeamService(AuctionRepository auctionRepository,
                              AuctionParticipantRepository auctionParticipantRepository,
                              FantasyTeamRepository fantasyTeamRepository,
                              AuthServiceClient authServiceClient) {
        this.auctionRepository = auctionRepository;
        this.auctionParticipantRepository = auctionParticipantRepository;
        this.fantasyTeamRepository = fantasyTeamRepository;
        this.authServiceClient = authServiceClient;
    }

    public FantasyTeam createTeam(String authorizationHeader, int auctionId, String teamName)
            throws TokenException, NotFoundException, ForbiddenException {

        AuthUserResponse user = authServiceClient.getAuthenticatedUser(authorizationHeader);

        if (user == null) {
            throw new TokenException("Invalid or expired token");
        }

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new NotFoundException("Auction with ID " + auctionId + " not found"));

        if (!auction.getCreatorUserId().equals(user.getId())) {
            throw new ForbiddenException("Only the auction owner can create teams.");
        }

        AuctionParticipant participant = auctionParticipantRepository
                .findByAuctionIdAndUserId(auctionId, user.getId())
                .orElseThrow(() -> new NotFoundException("You are not an approved participant of this auction."));

        if (!participant.isApproved()) {
            throw new ForbiddenException("You must be approved to create a team.");
        }

        if (fantasyTeamRepository.findByAuctionId(auctionId)
                .stream()
                .anyMatch(team -> team.getOwnerUserId().equals(user.getId()))) {
            throw new ForbiddenException("You already have a team in this auction.");
        }

        FantasyTeam team = new FantasyTeam();
        team.setAuctionId(auctionId);
        team.setOwnerUserId(user.getId());
        team.setName(teamName);
        team.setBudget(auction.getInitialCredits());
        team.setMaxGoalkeepers(auction.getMaxGoalkeepers());
        team.setMaxDefenders(auction.getMaxDefenders());
        team.setMaxMidfielders(auction.getMaxMidfielders());
        team.setMaxForwards(auction.getMaxForwards());

        return fantasyTeamRepository.save(team);
    }

    public List<FantasyTeam> listTeams(String authorizationHeader, int auctionId)
            throws TokenException, NotFoundException, ForbiddenException {

        AuthUserResponse user = authServiceClient.getAuthenticatedUser(authorizationHeader);

        if (user == null) {
            throw new TokenException("Invalid or expired token");
        }

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new NotFoundException("Auction with ID " + auctionId + " not found"));

        if (!auction.getCreatorUserId().equals(user.getId())) {
            throw new ForbiddenException("Only the auction owner can list teams.");
        }

        return fantasyTeamRepository.findByAuctionId(auctionId);
    }
}