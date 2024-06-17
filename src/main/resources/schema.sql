CREATE TABLE IF NOT EXISTS customer
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS address
(
    id          UUID PRIMARY KEY,
    street      VARCHAR(255) NOT NULL,
    city        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP,
    customer_id UUID,
    FOREIGN KEY (customer_id) REFERENCES customer (id)
);


-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--
-- CREATE TABLE IF NOT EXISTS customer (
--     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     name       VARCHAR(255) NOT NULL,
--     email      VARCHAR(255) NOT NULL,
--     created_at TIMESTAMP    NOT NULL,
--     updated_at TIMESTAMP
--     );
--
-- CREATE TABLE IF NOT EXISTS address (
--     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     street      VARCHAR(255) NOT NULL,
--     city        VARCHAR(255) NOT NULL,
--     created_at  TIMESTAMP    NOT NULL,
--     updated_at  TIMESTAMP,
--     customer_id UUID,
--     FOREIGN KEY (customer_id) REFERENCES customer (id)
--     );
