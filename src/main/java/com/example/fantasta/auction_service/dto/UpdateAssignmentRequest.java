package com.example.fantasta.auction_service.dto;

public class UpdateAssignmentRequest {

    private String role;
    private int cost;

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
}