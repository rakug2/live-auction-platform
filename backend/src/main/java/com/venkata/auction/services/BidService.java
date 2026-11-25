package com.venkata.auction.services;

import com.venkata.auction.DTO.Bid;
import com.venkata.auction.DTO.BidResponse;

public interface BidService {
    public BidResponse submitBid(Bid bid);
}
