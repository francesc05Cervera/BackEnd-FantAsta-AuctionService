package com.example.fantasta.auction_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "player_assignments")
public class PlayerAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer auctionId;

    @Column(nullable = false)
    private Integer teamId;

    @Column(nullable = false)
    private Integer playerId; // riferimento a PlayerService

    @Column(nullable = false, length = 50)
    private String role; // ruolo usato in questa asta (può differire dal ruolo globale)

    @Column(nullable = false)
    private int cost;

    public PlayerAssignment() {}
    public PlayerAssignment(Integer auctionId, Integer teamId, Integer playerId, String role, int cost) {
        this.auctionId = auctionId;
        this.teamId = teamId;
        this.playerId = playerId;
        this.role = role;
        this.cost = cost;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAuctionId() { return auctionId; }
    public void setAuctionId(Integer auctionId) { this.auctionId = auctionId; }

    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }

    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
}