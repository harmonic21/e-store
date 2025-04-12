CREATE TABLE IF NOT EXISTS e_store_user (
    id UUID PRIMARY KEY,
    username varchar(255) NOT NULL UNIQUE,
    password text NOT NULL,
    roles varchar[]
);

CREATE TABLE IF NOT EXISTS product (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL,
    description TEXT,
    image TEXT
);

CREATE TABLE IF NOT EXISTS product_order (
    id UUID PRIMARY KEY,
    order_sum NUMERIC NOT NULL DEFAULT 0,
    status VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,

    FOREIGN KEY(username) REFERENCES e_store_user(username)
);

CREATE TABLE IF NOT EXISTS basket (
    id UUID PRIMARY KEY,
    id_product UUID NOT NULL,
    product_count BIGINT NOT NULL DEFAULT 0,
    order_id UUID NOT NULL,

    FOREIGN KEY(id_product) REFERENCES product(id),
    FOREIGN KEY(order_id) REFERENCES product_order(id)
);