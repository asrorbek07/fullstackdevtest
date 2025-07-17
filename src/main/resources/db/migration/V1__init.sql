-- V1__init.sql

CREATE TABLE products (
                          id BIGINT PRIMARY KEY,
                          title TEXT NOT NULL,
                          vendor TEXT,
                          product_type TEXT,
                          image_url TEXT
);

CREATE TABLE variants (
                          id BIGINT PRIMARY KEY,
                          product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
                          title TEXT,
                          option2 TEXT, -- size
                          price NUMERIC(10,2), -- define precision and scale
                          available BOOLEAN DEFAULT TRUE
);
