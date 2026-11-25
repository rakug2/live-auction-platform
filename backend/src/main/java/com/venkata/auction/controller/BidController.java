package com.venkata.auction.controller;

import com.venkata.auction.DTO.Bid;
import com.venkata.auction.DTO.BidResponse;
import com.venkata.auction.services.BidService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bids")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    // @Valid runs the @NotNull/@Positive checks on Bid; a failure returns 400
    // before this method runs.
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BidResponse createBid(@Valid @RequestBody Bid bid) {
        return bidService.submitBid(bid);
    }
}
