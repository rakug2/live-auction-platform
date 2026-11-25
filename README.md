# Live Auction Platform

A real-time auction site where many people bid on the same item at once. The backend makes sure
bids are accepted in order for each auction and that exactly one winner is chosen when an auction
closes. Every bid and close is published to Kafka, keyed by auction ID, so separate services
(notifications, auction closing) each read the events at their own pace.

Built to learn Spring Boot and Kafka properly, one system-design topic at a time.

## Architecture

```
Client ──REST──► Spring Boot API ──► PostgreSQL (auctions, bids)
                       │
                       └─► Kafka topic "auction-events" (key = auctionId, 3 partitions)
                               ├─► consumer group "notifier"        → tells bidders they were outbid
                               └─► consumer group "auction-closer"  → closes the auction, picks the winner
```

Why the auction ID is the key: Kafka hashes the key to choose a partition, so every event for one
auction lands in the same partition and is read in order. Different auctions spread across the
partitions, so they are processed in parallel.

## Stack

Java 17, Spring Boot 4, Spring for Apache Kafka, Spring Data JPA, PostgreSQL 17, Flyway,
Apache Kafka 3.9 (KRaft, no ZooKeeper), Testcontainers, Docker Compose.

## Who built what

The backend (Spring Boot APIs, locking, Kafka producers and consumers, outbox, error handling) is
written by me. Local infrastructure, test scaffolding and the README were set up with AI
assistance (Claude Code), and I reviewed and can explain all of it.

## Run it locally

Needs Docker and Java 17.

```bash
docker compose up -d          # PostgreSQL on 5433, Kafka on 29092, Kafka UI on http://localhost:8090
cd backend
./mvnw spring-boot:run        # API on http://localhost:8080
curl localhost:8080/actuator/health
```

Stop everything with `docker compose down` (add `-v` to delete the database).

## Roadmap

Each step runs on its own before the next one starts.

| # | Step | Status |
|---|---|---|
| 0 | Skeleton: Spring Boot app, PostgreSQL, Flyway, Kafka in Docker, health check | Done |
| K0 | Kafka by hand with the CLI: create `auction-events` with 3 partitions, send keyed messages, two consumers in one group, look at offsets and lag, replay by resetting offsets | Done |
| 1 | Auctions and bids: `POST /auctions`, `POST /auctions/{id}/bids`, `GET /auctions/{id}` | Next |
| 2 | Producer: publish `BidPlaced` with `KafkaTemplate`, keyed by auction ID, `acks=all` | |
| 3 | Consumer: `notifier` group with `@KafkaListener`, manual acknowledgement after processing | |
| 4 | Second group: `auction-closer` reads the same events and closes expired auctions, publishing `AuctionClosed` | |
| 5 | Idempotency: a `processed_events` table so a re-delivered event is applied once | |
| 6 | Failures: retries, then a dead-letter topic for events that keep failing | |
| 7 | Transactional outbox: save the bid and the event in one database transaction, then publish | |
| 8 | Tests: Testcontainers integration tests for each step above | |

Later, one topic at a time: concurrent bids with row locking, Redis caching, live updates over
WebSockets, rate limiting, sharding bids by auction ID.
