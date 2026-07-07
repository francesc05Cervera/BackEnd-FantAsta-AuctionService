package com.example.fantasta.auction_service.controller;

import com.example.fantasta.auction_service.entity.PlayerAssignment;
import com.example.fantasta.auction_service.service.PlayerAssignmentService;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.exception.ForbiddenException;
import com.example.fantasta.auction_service.exception.BudgetExceededException;
import com.example.fantasta.auction_service.exception.LimitExceededException;
import com.example.fantasta.auction_service.exception.DuplicateAssignmentException;
import com.example.fantasta.auction_service.dto.AssignPlayerRequest;



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

    @PostMapping("/{auctionId}")
    public ResponseEntity<?> assignPlayer(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int auctionId,
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

    @GetMapping("/{auctionId}")
    public ResponseEntity<?> listAssignments(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int auctionId) {

        try {
            List<PlayerAssignment> assignments = playerAssignmentService.listAssignments(authorizationHeader, auctionId);
            return ResponseEntity.ok(assignments);
        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    
}

