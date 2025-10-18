-- V2__Add_orders_and_image_url.sql
-- Add orders tables and file_url column to product_images

-- Add file_url column to product_images table
ALTER TABLE product_images 
ADD COLUMN file_url VARCHAR(500);

COMMENT ON COLUMN product_images.file_url IS 'Public URL to access the image file';

-- Create orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    customer_name VARCHAR(100) NOT NULL,
    customer_email VARCHAR(100),
    customer_phone VARCHAR(20),
    shipping_address VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    subtotal DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    tax_amount DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    shipping_cost DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    discount_amount DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    platform VARCHAR(50),
    platform_user_id VARCHAR(100),
    notes VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT chk_order_subtotal CHECK (subtotal >= 0),
    CONSTRAINT chk_order_tax CHECK (tax_amount >= 0),
    CONSTRAINT chk_order_shipping CHECK (shipping_cost >= 0),
    CONSTRAINT chk_order_discount CHECK (discount_amount >= 0),
    CONSTRAINT chk_order_total CHECK (total_amount >= 0)
);

CREATE INDEX idx_order_number ON orders(order_number);
CREATE INDEX idx_order_status ON orders(status);
CREATE INDEX idx_order_customer_email ON orders(customer_email);
CREATE INDEX idx_order_platform ON orders(platform, platform_user_id);
CREATE INDEX idx_order_created_at ON orders(created_at);

-- Create order_items table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    product_sku VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    line_total DECIMAL(12, 2) NOT NULL,
    product_image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT chk_order_item_quantity CHECK (quantity > 0),
    CONSTRAINT chk_order_item_unit_price CHECK (unit_price >= 0),
    CONSTRAINT chk_order_item_line_total CHECK (line_total >= 0)
);

CREATE INDEX idx_order_item_order ON order_items(order_id);
CREATE INDEX idx_order_item_product ON order_items(product_id);

-- Update inventory_transactions table to reference orders
ALTER TABLE inventory_transactions
ADD COLUMN inventory_id BIGINT,
ADD COLUMN previous_quantity INT,
ADD COLUMN new_quantity INT,
ADD COLUMN reason VARCHAR(500);

-- Add foreign key for inventory_id
ALTER TABLE inventory_transactions
ADD CONSTRAINT fk_inventory_transaction_inventory 
FOREIGN KEY (inventory_id) REFERENCES inventory(id);

-- Rename columns for clarity
ALTER TABLE inventory_transactions
RENAME COLUMN product_id TO _deprecated_product_id;
ALTER TABLE inventory_transactions
RENAME COLUMN quantity_change TO quantity;
ALTER TABLE inventory_transactions
RENAME COLUMN quantity_after TO _deprecated_quantity_after;
ALTER TABLE inventory_transactions
RENAME COLUMN notes TO _deprecated_notes;
ALTER TABLE inventory_transactions
RENAME COLUMN reference TO _deprecated_reference;

CREATE INDEX idx_inventory_transaction_inventory ON inventory_transactions(inventory_id);

COMMENT ON TABLE orders IS 'Customer orders and sales records';
COMMENT ON TABLE order_items IS 'Line items within orders with product snapshots';
COMMENT ON COLUMN order_items.product_name IS 'Snapshot of product name at time of order';
COMMENT ON COLUMN order_items.product_sku IS 'Snapshot of product SKU at time of order';
COMMENT ON COLUMN order_items.unit_price IS 'Price per unit at time of order';
COMMENT ON COLUMN order_items.product_image_url IS 'Primary product image URL at time of order';

