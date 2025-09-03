CREATE TABLE IF NOT EXISTS processed_message(
    id BIGINT PRIMARY KEY,
    message TEXT NOT NULL,
    type VARCHAR(30) NOT NULL,
    processed_at TIMESTAMP NOT NULL DEFAULT current_timestamp
);