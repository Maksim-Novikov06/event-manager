CREATE TABLE events
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    owner_id    BIGINT       NOT NULL,
    max_places  INT          NOT NULL,
    date        TIMESTAMP    NOT NULL,
    cost        INT          NOT NULL,
    duration    INT          NOT NULL,
    location_id INT          NOT NULL,
    status      VARCHAR(255) NOT NULL
);