CREATE TABLE registrations (
                               id SERIAL PRIMARY KEY,
                               user_id BIGINT,
                               event_id BIGINT NOT NULL,
                               date_registration TIMESTAMP,

                               FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);