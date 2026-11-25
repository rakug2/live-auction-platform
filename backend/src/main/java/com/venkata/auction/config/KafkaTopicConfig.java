package com.venkata.auction.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String BIDS_TOPIC = "bids.placed";

    // KafkaAdmin creates this topic at startup if it doesn't exist.
    @Bean
    public NewTopic bidsTopic() {
        return TopicBuilder.name(BIDS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}