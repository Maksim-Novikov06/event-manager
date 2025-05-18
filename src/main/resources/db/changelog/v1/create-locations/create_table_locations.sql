CREATE TABLE locations
(
    id          serial PRIMARY KEY,
    name        varchar(150) NOT NULL UNIQUE ,
    address     varchar(150) NOT NULL,
    capacity    bigint    NOT NULL,
    description varchar(150)
);