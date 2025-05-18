CREATE TABLE users
(
    id       serial PRIMARY KEY,
    login    varchar(50) NOT NULL UNIQUE,
    password varchar NOT NULL,
    role     varchar(5)  NOT NULL

);