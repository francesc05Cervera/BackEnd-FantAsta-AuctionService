package com.example.fantasta.auction_service.dto;

public class AssignPlayerRequest {
    private int teamId;
    private int playerId;
    private String role;
    private int cost;

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
} 
  

