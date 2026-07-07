package com.example.fantasta.auction_service.dto;

public class UpdateAuctionRequest {
    private String name;
    private Integer maxPlayersPerTeam;
    private Integer maxGoalkeepers;
    private Integer maxDefenders;
    private Integer maxMidfielders;
    private Integer maxForwards;
    private Integer initialCredits;

    public UpdateAuctionRequest() {
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getMaxPlayersPerTeam() { return maxPlayersPerTeam; }
    public void setMaxPlayersPerTeam(Integer maxPlayersPerTeam) { this.maxPlayersPerTeam = maxPlayersPerTeam; }

    public Integer getMaxGoalkeepers() { return maxGoalkeepers; }
    public void setMaxGoalkeepers(Integer maxGoalkeepers) { this.maxGoalkeepers = maxGoalkeepers; }

    public Integer getMaxDefenders() { return maxDefenders; }
    public void setMaxDefenders(Integer maxDefenders) { this.maxDefenders = maxDefenders; }

    public Integer getMaxMidfielders() { return maxMidfielders; }
    public void setMaxMidfielders(Integer maxMidfielders) { this.maxMidfielders = maxMidfielders; }

    public Integer getMaxForwards() { return maxForwards; }
    public void setMaxForwards(Integer maxForwards) { this.maxForwards = maxForwards; }

    public Integer getInitialCredits() { return initialCredits; }
    public void setInitialCredits(Integer initialCredits) { this.initialCredits = initialCredits; }
}