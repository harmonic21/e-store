CREATE TABLE IF NOT EXISTS product (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL,
    description TEXT,
    image TEXT
);

CREATE TABLE IF NOT EXISTS basket (
    id VARCHAR(36) PRIMARY KEY,
    id_product BIGINT NOT NULL,
    product_count BIGINT NOT NULL DEFAULT 0,

    FOREIGN KEY(id_product) REFERENCES product(id)
)