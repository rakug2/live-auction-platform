package com.venkata.auction.DTO;

// The 202 body: {"bidId": "...", "status": "ACCEPTED"}
public class BidResponse {

    private final String bidId;
    private final String status;

    public BidResponse(String bidId, String status) {
        this.bidId = bidId;
        this.status = status;
    }

    public String getBidId() {
        return bidId;
    }

    public String getStatus() {
        return status;
    }
}
