package com.example.fantasta.auction_service.dto;

import java.time.LocalDateTime;

import com.example.fantasta.auction_service.enumeration.AuctionStatus;

public class AuctionResponse {

    private int id;
    private String name;
    private String auctionCode;
    private Long creatorUserId;
    private int maxPlayersPerTeam;
    private int maxGoalkeepers;
    private int maxDefenders;
    private int maxMidfielders;
    private int maxForwards;
    private int initialCredits;
    private AuctionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AuctionResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuctionCode() {
        return auctionCode;
    }

    public void setAuctionCode(String auctionCode) {
        this.auctionCode = auctionCode;
    }

    public Long getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(Long long1) {
        this.creatorUserId = long1;
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

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus auctionStatus) {
        this.status = auctionStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}