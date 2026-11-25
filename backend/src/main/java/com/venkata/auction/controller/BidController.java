package com.venkata.auction.controller;

import com.venkata.auction.DTO.Bid;
import com.venkata.auction.DTO.BidResponse;
import com.venkata.auction.services.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bids")
public class BidController {
    private final BidService bidService;
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }
    

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BidResponse createBid(@RequestBody Bid bid){
        BidResponse bidResponse;
        bidResponse = bidService.submitBid(bid);
        return bidResponse;
    }
}
