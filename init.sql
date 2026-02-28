-- init.sql
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS locations (
    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),
    country VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    reference_id BIGINT PRIMARY KEY,

    CONSTRAINT fk_location_user
    FOREIGN KEY (reference_id)
    REFERENCES users(id)
    ON DELETE CASCADE
);

CREATE INDEX idx_name ON locations (country, city);


INSERT INTO users (name) VALUES ('Alice'), ('Bob');
