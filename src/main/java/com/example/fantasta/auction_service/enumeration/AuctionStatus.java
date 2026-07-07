package com.example.fantasta.auction_service.enumeration;

public enum AuctionStatus {
    CREATED,
    OPEN,
    CLOSED;

    public static AuctionStatus fromString(String status) 
    {
        for (AuctionStatus auctionStatus : AuctionStatus.values()) {
            if (auctionStatus.name().equalsIgnoreCase(status)) {
                return auctionStatus;
            }
        }
        throw new IllegalArgumentException("Invalid auction status: " + status);
    }
}