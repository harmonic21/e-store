CREATE TABLE IF NOT EXISTS product (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL,
    description TEXT,
    image TEXT
);

CREATE TABLE IF NOT EXISTS product_order (
    id VARCHAR(36) PRIMARY KEY,
    order_sum NUMERIC NOT NULL DEFAULT 0,
    status VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS basket (
    id VARCHAR(36) PRIMARY KEY,
    id_product VARCHAR(36) NOT NULL,
    product_count BIGINT NOT NULL DEFAULT 0,
    order_id VARCHAR(36) NOT NULL,

    FOREIGN KEY(id_product) REFERENCES product(id),
    FOREIGN KEY(order_id) REFERENCES product_order(id)
);