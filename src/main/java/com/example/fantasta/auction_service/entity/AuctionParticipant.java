package com.example.fantasta.auction_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auction_participants",
       uniqueConstraints = @UniqueConstraint(columnNames = {"auction_id", "user_id"}))
public class AuctionParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "auction_id", nullable = false)
    private int auctionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @Column(nullable = false)
    private boolean approved = false;

    public AuctionParticipant() {
    }

    public AuctionParticipant(int id, int auctionId, Long userId, LocalDateTime joinedAt) {
        this.id = id;
        this.auctionId = auctionId;
        this.userId = userId;
        this.joinedAt = joinedAt;
        this.approved = false;
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

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }
    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
} 
    

