package com.venkata.auction.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

// HTTP request body for POST /bids. Jackson builds it with the no-arg
// constructor and the setters. No bidId here: the server creates it.
public class Bid {

    @NotNull
    private Long auctionId;

    @NotNull
    private Long bidderId;

    @NotNull
    @Positive
    private BigDecimal bidAmount;   // BigDecimal for money: no rounding errors

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public Long getBidderId() {
        return bidderId;
    }

    public void setBidderId(Long bidderId) {
        this.bidderId = bidderId;
    }

    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }
}
