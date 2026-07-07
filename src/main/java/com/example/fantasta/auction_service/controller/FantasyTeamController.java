package com.example.fantasta.auction_service.controller;

import com.example.fantasta.auction_service.entity.FantasyTeam;
import com.example.fantasta.auction_service.service.FantasyTeamService;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.exception.ForbiddenException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class FantasyTeamController {

    private final FantasyTeamService fantasyTeamService;

    public FantasyTeamController(FantasyTeamService fantasyTeamService) {
        this.fantasyTeamService = fantasyTeamService;
    }

    @PostMapping("/{auctionId}")
    public ResponseEntity<?> createTeam(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int auctionId,
            @RequestBody String teamName) {

        try {
            FantasyTeam team = fantasyTeamService.createTeam(authorizationHeader, auctionId, teamName);
            return ResponseEntity.ok(team);
        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/{auctionId}")
    public ResponseEntity<?> listTeams(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int auctionId) {

        try {
            List<FantasyTeam> teams = fantasyTeamService.listTeams(authorizationHeader, auctionId);
            return ResponseEntity.ok(teams);
        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}