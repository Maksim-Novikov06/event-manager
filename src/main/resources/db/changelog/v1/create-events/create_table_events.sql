CREATE TABLE locations
(
    id          serial PRIMARY KEY,
    name        varchar(150) NOT NULL UNIQUE ,
    date        date NOT NULL,
    duration    bigint NOT NULL,
    cost        bigint NOT NULL,

    capacity    bigint    NOT NULL,
    description varchar(150)
);