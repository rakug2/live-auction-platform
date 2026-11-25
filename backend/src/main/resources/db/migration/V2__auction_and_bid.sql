CREATE TABLE auction (
    id              BIGSERIAL      PRIMARY KEY,
    title           VARCHAR(200)   NOT NULL,
    seller          VARCHAR(100)   NOT NULL,
    starting_price  NUMERIC(12,2)  NOT NULL CHECK (starting_price > 0),
    closes_at       TIMESTAMPTZ    NOT NULL,
    status          VARCHAR(20)    NOT NULL DEFAULT 'OPEN'
                                   CHECK (status IN ('OPEN', 'CLOSED')),
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT now()
);

CREATE TABLE bid (
    id          BIGSERIAL      PRIMARY KEY,
    auction_id  BIGINT         NOT NULL REFERENCES auction(id),
    bidder      VARCHAR(100)   NOT NULL,
    amount      NUMERIC(12,2)  NOT NULL CHECK (amount > 0),
    request_id  UUID           NOT NULL UNIQUE,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT now()
);

-- "Highest bid for auction X" reads this index top-down instead of scanning every bid.
CREATE INDEX idx_bid_auction_amount ON bid (auction_id, amount DESC);
