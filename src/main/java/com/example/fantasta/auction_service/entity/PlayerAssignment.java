package com.example.fantasta.auction_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "player_assignments")
public class PlayerAssignment 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "auction_id", nullable = false)
    private int auctionId;

    @Column(name = "team_id", nullable = false)
    private int teamId;

    @Column(name = "player_id", nullable = false)
    private int playerId;

    @Column(name = "role", nullable = false)
    private String role; // PORTIERE, DIFENSORE, CENTROCAMPISTA, ATTACCANTE

    @Column(nullable = false)
    private int cost;

    @Column(name = "owner_user_id", nullable = false)
    private Long ownerUserId;

    @Column(name = "assigned_at", nullable = false)
    private java.time.LocalDateTime assignedAt;

    public PlayerAssignment() {
    }

    public PlayerAssignment(int id, int auctionId, int teamId, int playerId, String role, int cost, Long ownerUserId, LocalDateTime assignedAt) {
        this.id = id;
        this.auctionId = auctionId;
        this.teamId = teamId;
        this.playerId = playerId;
        this.role = role;
        this.cost = cost;
        this.ownerUserId = ownerUserId;
        this.assignedAt = assignedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}
