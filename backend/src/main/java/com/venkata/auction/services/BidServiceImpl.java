package com.venkata.auction.services;

import com.venkata.auction.DTO.Bid;
import com.venkata.auction.DTO.BidEvent;
import com.venkata.auction.DTO.BidResponse;
import com.venkata.auction.config.KafkaTopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class BidServiceImpl implements BidService {

    private static final Logger log = LoggerFactory.getLogger(BidServiceImpl.class);

    private final KafkaTemplate<String, BidEvent> kafkaTemplate;

    public BidServiceImpl(KafkaTemplate<String, BidEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public BidResponse submitBid(Bid bid) {
        // Generated ONCE; the same id goes in the event and the response.
        String bidId = bid.getAuctionId() + "-" + bid.getBidderId() + "-" + UUID.randomUUID();

        BidEvent bidEvent = new BidEvent();
        bidEvent.setBidId(bidId);
        bidEvent.setAuctionId(bid.getAuctionId());
        bidEvent.setUserId(bid.getBidderId());
        bidEvent.setPrice(bid.getBidAmount());
        bidEvent.setPlacedAt(Instant.now());

        // Key = auctionId ONLY, so every bid for one auction goes to the same partition, in order.
        String key = String.valueOf(bid.getAuctionId());

        try {
            SendResult<String, BidEvent> result = kafkaTemplate
                    .send(KafkaTopicConfig.BIDS_TOPIC, key, bidEvent)
                    .get(5, TimeUnit.SECONDS);   // wait for the broker's ack, max 5 s

            // getRecordMetadata() = where Kafka actually stored it (the ack).
            log.info("Published bid {} to {} partition {} offset {}",
                    bidId,
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Interrupted while publishing bid", e);
        } catch (ExecutionException | TimeoutException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Kafka did not acknowledge the bid", e);
        }

        return new BidResponse(bidId, "ACCEPTED");
    }
}
