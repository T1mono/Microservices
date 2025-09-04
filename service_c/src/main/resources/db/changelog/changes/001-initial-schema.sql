--liquibase formatted sql
--changeset your_name:1

CREATE TABLE IF NOT EXISTS processed_messages (
                                                  id BIGSERIAL PRIMARY KEY,                 -- BIGSERIAL вместо BIGINT для автоинкремента
                                                  message TEXT NOT NULL,
                                                  type VARCHAR(50) NOT NULL,                -- Увеличил до 50 символов на будущее
                                                  processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--rollback DROP TABLE IF EXISTS processed_messages;  -- Добавил IF EXISTS для безопасности

--changeset your_name:2

CREATE INDEX IF NOT EXISTS idx_processed_messages_type ON processed_messages(type);
CREATE INDEX IF NOT EXISTS idx_processed_messages_processed_at ON processed_messages(processed_at);

--rollback DROP INDEX IF EXISTS idx_processed_messages_type;
--rollback DROP INDEX IF EXISTS idx_processed_messages_processed_at;