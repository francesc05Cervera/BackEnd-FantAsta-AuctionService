package com.example.fantasta.auction_service.dto;

public class CreateAuctionRequest {

    private String name;
    private int maxPlayersPerTeam;
    private int maxGoalkeepers;
    private int maxDefenders;
    private int maxMidfielders;
    private int maxForwards;
    private int initialCredits;

    public CreateAuctionRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public void setMaxPlayersPerTeam(int maxPlayersPerTeam) {
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    public int getMaxGoalkeepers() {
        return maxGoalkeepers;
    }

    public void setMaxGoalkeepers(int maxGoalkeepers) {
        this.maxGoalkeepers = maxGoalkeepers;
    }

    public int getMaxDefenders() {
        return maxDefenders;
    }

    public void setMaxDefenders(int maxDefenders) {
        this.maxDefenders = maxDefenders;
    }

    public int getMaxMidfielders() {
        return maxMidfielders;
    }

    public void setMaxMidfielders(int maxMidfielders) {
        this.maxMidfielders = maxMidfielders;
    }

    public int getMaxForwards() {
        return maxForwards;
    }

    public void setMaxForwards(int maxForwards) {
        this.maxForwards = maxForwards;
    }

    public int getInitialCredits() {
        return initialCredits;
    }

    public void setInitialCredits(int initialCredits) {
        this.initialCredits = initialCredits;
    }
}