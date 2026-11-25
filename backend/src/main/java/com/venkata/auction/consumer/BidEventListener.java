package com.venkata.auction.consumer;

import com.venkata.auction.DTO.BidEvent;
import com.venkata.auction.config.KafkaTopicConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BidEventListener {
    private static final Logger log = LoggerFactory.getLogger(BidEventListener.class);

    @KafkaListener(topics= KafkaTopicConfig.BIDS_TOPIC)
    public void onBidEvent(ConsumerRecord<String, BidEvent> record) {
        BidEvent bidEvent = record.value();
        log.info("Received a bid event from topic {}", record.topic());
        log.info("Received a bid event from topic {}", record.key());
        log.info("Bid event data is bid id {}, auction id {}, bidder id {}" , bidEvent.getBidId(), bidEvent.getAuctionId(), bidEvent.getUserId());
    }

}
