package com.venkata.auction.DTO;

import java.math.BigDecimal;
import java.time.Instant;

// The message published to Kafka. Jackson turns it into JSON using the getters.
public class BidEvent {

    private String bidId;       // same id returned to the user, used for dedupe later
    private Long auctionId;
    private Long userId;
    private BigDecimal price;
    private Instant placedAt;

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(Instant placedAt) {
        this.placedAt = placedAt;
    }
}
