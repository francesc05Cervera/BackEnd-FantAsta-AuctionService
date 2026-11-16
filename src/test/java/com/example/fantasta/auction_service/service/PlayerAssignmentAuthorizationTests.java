package com.example.fantasta.auction_service.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.example.fantasta.auction_service.client.AuthServiceClient;
import com.example.fantasta.auction_service.dto.AuthUserResponse;
import com.example.fantasta.auction_service.entity.PlayerAssignment;
import com.example.fantasta.auction_service.entity.Auction;
import com.example.fantasta.auction_service.exception.ForbiddenException;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.repository.PlayerAssignmentRepository;
import com.example.fantasta.auction_service.repository.AuctionRepository;
import com.example.fantasta.auction_service.repository.FantasyTeamRepository;
import com.example.fantasta.auction_service.repository.AuctionParticipantRepository;
import com.example.fantasta.auction_service.client.PlayerServiceClient;
import org.junit.jupiter.api.Test;

class PlayerAssignmentAuthorizationTests {
    private final PlayerAssignmentRepository assignments = mock(PlayerAssignmentRepository.class);
    private final AuthServiceClient auth = mock(AuthServiceClient.class);
    private final AuctionRepository auctions = mock(AuctionRepository.class);
    private final FantasyTeamRepository teams = mock(FantasyTeamRepository.class);
    private final AuctionParticipantRepository participants = mock(AuctionParticipantRepository.class);
    private final PlayerServiceClient players = mock(PlayerServiceClient.class);
    private final PlayerAssignmentService service = new PlayerAssignmentService(assignments, auctions, teams, participants, auth, players);

    @Test
    void invalidCredentialsDoNotReachAssignmentRepository() throws Exception {
        when(auth.getAuthenticatedUser("arbitrary")).thenThrow(new TokenException("Invalid token"));
        assertThrows(TokenException.class, () -> service.listAssignmentsByAuction("arbitrary", 1));
        assertThrows(TokenException.class, () -> service.listAssignmentsByAuctionAndTeam("arbitrary", 1, 2));
        assertThrows(TokenException.class, () -> service.updateAssignmentRoleCost("arbitrary", 1, "P", 1));
        verifyNoInteractions(assignments);
    }

    @Test
    void onlyTheAuctionOwnerMayUpdateAnAssignment() throws Exception {
        var assignment = new PlayerAssignment();
        assignment.setAuctionId(3);
        when(assignments.findById(1)).thenReturn(Optional.of(assignment));
        when(auth.getAuthenticatedUser("Bearer valid")).thenReturn(new AuthUserResponse(7L, "a@b.test", "A", "B"));
        Auction auction = new Auction();
        auction.setCreatorUserId(8L);
        when(auctions.findWithLockById(3)).thenReturn(Optional.of(auction));

        assertThrows(ForbiddenException.class, () -> service.updateAssignmentRoleCost("Bearer valid", 1, "P", 1));
        verify(assignments, never()).save(assignment);
        verify(assignments, never()).save(assignment);
    }
}
