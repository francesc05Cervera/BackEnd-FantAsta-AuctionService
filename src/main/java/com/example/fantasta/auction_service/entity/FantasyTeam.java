package com.example.fantasta.auction_service.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fantasy_teams")
public class FantasyTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "auction_id", nullable = false)
    private int auctionId;

    @Column(name = "owner_user_id", nullable = false)
    private Long ownerUserId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int budget;

    @Column(nullable = false)
    private int maxGoalkeepers;

    @Column(nullable = false)
    private int maxDefenders;

    @Column(nullable = false)
    private int maxMidfielders;

    @Column(nullable = false)
    private int maxForwards;

    public FantasyTeam() {
    }

    public FantasyTeam(int auctionId, Long ownerUserId, String name, int budget,
                       int maxGoalkeepers, int maxDefenders, int maxMidfielders, int maxForwards) {
        this.auctionId = auctionId;
        this.ownerUserId = ownerUserId;
        this.name = name;
        this.budget = budget;
        this.maxGoalkeepers = maxGoalkeepers;
        this.maxDefenders = maxDefenders;
        this.maxMidfielders = maxMidfielders;
        this.maxForwards = maxForwards;
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

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
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
}