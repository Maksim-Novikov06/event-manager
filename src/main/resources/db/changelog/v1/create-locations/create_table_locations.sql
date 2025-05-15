CREATE TABLE locations
(
    id          serial PRIMARY KEY,
    name        varchar(15) NOT NULL,
    address     varchar(15) NOT NULL,
    capacity    bigint      NOT NULL,
    description varchar(50) NOT NULL
);