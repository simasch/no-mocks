CREATE SEQUENCE customer_seq START WITH 100000 INCREMENT BY 50;

CREATE TABLE customer
(
    id          BIGINT  NOT NULL DEFAULT nextval('customer_seq') PRIMARY KEY,
    first_name  VARCHAR NOT NULL,
    last_name   VARCHAR NOT NULL,
    street      VARCHAR NOT NULL,
    postal_code VARCHAR NOT NULL,
    city        VARCHAR NOT NULL
);

CREATE SEQUENCE product_seq START WITH 100000 INCREMENT BY 50;

CREATE TABLE product
(
    id    BIGINT  NOT NULL DEFAULT nextval('product_seq') PRIMARY KEY,
    name  VARCHAR NOT NULL,
    price DOUBLE PRECISION
);

CREATE SEQUENCE purchase_order_seq START WITH 100000 INCREMENT BY 50;

CREATE TABLE purchase_order
(
    id          BIGINT    NOT NULL DEFAULT nextval('purchase_order_seq') PRIMARY KEY,
    order_date  TIMESTAMP NOT NULL,

    customer_id BIGINT    NOT NULL REFERENCES customer (id)
);

CREATE INDEX ux_purchase_order_customer_id ON purchase_order (customer_id);


CREATE SEQUENCE order_item_seq START WITH 100000 INCREMENT BY 50;

CREATE TABLE order_item
(
    id                BIGINT NOT NULL DEFAULT nextval('order_item_seq') PRIMARY KEY,
    quantity          INT    NOT NULL,

    purchase_order_id BIGINT NOT NULL REFERENCES purchase_order (id),
    product_id        BIGINT NOT NULL REFERENCES product (id)
);

CREATE INDEX ux_order_item_purchase_order_id ON order_item (purchase_order_id);
CREATE INDEX ux_order_item_product_id ON order_item (product_id);
