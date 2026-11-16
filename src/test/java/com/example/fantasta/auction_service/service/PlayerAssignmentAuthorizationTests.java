package com.example.fantasta.auction_service.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.example.fantasta.auction_service.client.AuthServiceClient;
import com.example.fantasta.auction_service.dto.AuthUserResponse;
import com.example.fantasta.auction_service.entity.PlayerAssignment;
import com.example.fantasta.auction_service.exception.ForbiddenException;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.repository.PlayerAssignmentRepository;
import org.junit.jupiter.api.Test;

class PlayerAssignmentAuthorizationTests {
    private final PlayerAssignmentRepository assignments = mock(PlayerAssignmentRepository.class);
    private final AuthServiceClient auth = mock(AuthServiceClient.class);
    private final AuctionService auctions = mock(AuctionService.class);
    private final PlayerAssignmentService service = new PlayerAssignmentService(assignments, auth, auctions, null, null);

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
        var assignment = mock(PlayerAssignment.class);
        when(assignment.getAuctionId()).thenReturn(3);
        when(assignments.findById(1)).thenReturn(Optional.of(assignment));
        when(auth.getAuthenticatedUser("Bearer valid")).thenReturn(new AuthUserResponse(7L, "a@b.test", "A", "B"));

        assertThrows(ForbiddenException.class, () -> service.updateAssignmentRoleCost("Bearer valid", 1, "P", 1));
        verify(assignments, never()).save(assignment);
        verify(assignment, never()).setCost(1);

        when(auctions.isOwner(3, 7L)).thenReturn(true);
        service.updateAssignmentRoleCost("Bearer valid", 1, "P", 1);
        verify(assignment).setRole("P");
        verify(assignment).setCost(1);
        verify(assignments).save(assignment);
    }
}
