package com.example.fantasta.auction_service.dto;

public class AssignPlayerRequest {

    private Integer teamId;
    private Integer playerId;
    private String role;
    private int cost;

    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }

    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
}