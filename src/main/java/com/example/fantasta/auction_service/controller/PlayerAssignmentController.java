package com.example.fantasta.auction_service.controller;

import com.example.fantasta.auction_service.dto.AssignPlayerRequest;
import com.example.fantasta.auction_service.dto.UpdateAssignmentRequest;
import com.example.fantasta.auction_service.entity.PlayerAssignment;
import com.example.fantasta.auction_service.service.PlayerAssignmentService;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.exception.ForbiddenException;
import com.example.fantasta.auction_service.exception.BudgetExceededException;
import com.example.fantasta.auction_service.exception.LimitExceededException;
import com.example.fantasta.auction_service.exception.DuplicateAssignmentException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class PlayerAssignmentController {

    private final PlayerAssignmentService playerAssignmentService;

    public PlayerAssignmentController(PlayerAssignmentService playerAssignmentService) {
        this.playerAssignmentService = playerAssignmentService;
    }

    // 1. POST /api/assignments/{auctionId}
    @PostMapping("/{auctionId}")
    public ResponseEntity<?> assignPlayer(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer auctionId,
            @RequestBody AssignPlayerRequest request) {

        try {
            PlayerAssignment assignment = playerAssignmentService.assignPlayer(
                    authorizationHeader,
                    auctionId,
                    request.getTeamId(),
                    request.getPlayerId(),
                    request.getRole(),
                    request.getCost()
            );
            return ResponseEntity.ok(assignment);
        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (BudgetExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (LimitExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicateAssignmentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 2. GET /api/assignments/{auctionId}
    @GetMapping("/{auctionId}")
    public ResponseEntity<?> listAssignmentsByAuction(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer auctionId) {

        try {
            List<PlayerAssignment> assignments = playerAssignmentService.listAssignmentsByAuction(auctionId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 3. GET /api/assignments/{auctionId}/team/{teamId}
    @GetMapping("/{auctionId}/team/{teamId}")
    public ResponseEntity<?> listAssignmentsByAuctionAndTeam(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer auctionId,
            @PathVariable Integer teamId) {

        try {
            List<PlayerAssignment> assignments = playerAssignmentService.listAssignmentsByAuctionAndTeam(auctionId, teamId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 4. PUT /api/assignments/{assignmentId}
    @PutMapping("/{assignmentId}")
    public ResponseEntity<?> updateAssignmentRoleCost(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer assignmentId,
            @RequestBody UpdateAssignmentRequest request) {

        try {
            PlayerAssignment assignment = playerAssignmentService.updateAssignmentRoleCost(
                    assignmentId,
                    request.getRole(),
                    request.getCost()
            );
            return ResponseEntity.ok(assignment);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}