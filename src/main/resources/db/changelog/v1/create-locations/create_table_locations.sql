CREATE TABLE locations
(
    id          serial PRIMARY KEY,
    name        varchar(50) NOT NULL,
    address     varchar(50) NOT NULL,
    capacity    bigint    NOT NULL,
    description varchar(50)
);