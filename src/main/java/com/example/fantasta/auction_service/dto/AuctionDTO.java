package com.example.fantasta.auction_service.dto;

import com.example.fantasta.auction_service.enumeration.AuctionStatus;


public class AuctionDTO 
{
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
    
    public AuctionDTO() {}

	public AuctionDTO(String name, String auctionCode, Long creatorUserId, int maxPlayersPerTeam, int maxGoalkeepers,
			int maxDefenders, int maxMidfielders, int maxForwards, int initialCredits, AuctionStatus status) {
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

	public void setCreatorUserId(Long creatorUserId) {
		this.creatorUserId = creatorUserId;
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
    
	
}
