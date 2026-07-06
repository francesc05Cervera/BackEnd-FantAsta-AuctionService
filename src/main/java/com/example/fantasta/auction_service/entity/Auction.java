package com.example.fantasta.auction_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.fantasta.auction_service.enumeration.AuctionStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "auctions")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String auctionCode;

    @Column(nullable = false)
    private Long creatorUserId;

    @Column(nullable = false)
    private int maxPlayersPerTeam;

    @Column(nullable = false)
    private int maxGoalkeepers;

    @Column(nullable = false)
    private int maxDefenders;

    @Column(nullable = false)
    private int maxMidfielders;

    @Column(nullable = false)
    private int maxForwards;

    @Column(nullable = false)
    private int initialCredits;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AuctionStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Auction() {
    }

    public Auction(int id, String name, String auctionCode, Long creatorUserId,
                   int maxPlayersPerTeam, int maxGoalkeepers, int maxDefenders,
                   int maxMidfielders, int maxForwards, int initialCredits,
                   AuctionStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) 
    {
        this.id = id;
        this.name = name;
        this.auctionCode = auctionCode;
        this.creatorUserId = creatorUserId;
        this.maxPlayersPerTeam = maxPlayersPerTeam;
        this.maxGoalkeepers = maxGoalkeepers;
        this.maxDefenders = maxDefenders;
        this.maxMidfielders = maxMidfielders;
        this.maxForwards = maxForwards;
        this.initialCredits = initialCredits;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public void setStatus(AuctionStatus status) {
        this.status = status;
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