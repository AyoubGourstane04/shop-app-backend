ALTER TABLE products
    ADD COLUMN image_url TEXT,
    ADD COLUMN category_id VARCHAR(64),
    ADD COLUMN is_featured BOOLEAN NOT NULL DEFAULT FALSE;

CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_featured ON products(is_featured);
CREATE INDEX idx_products_price ON products(price);
