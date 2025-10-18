-- V2__Sample_data.sql
-- Sample data for testing and demonstration

-- Insert sample categories
INSERT INTO categories (name, description, display_order, active, created_at, updated_at, deleted) VALUES
('Clothing', 'All clothing items', 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Electronics', 'Electronic devices and accessories', 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Home & Garden', 'Home improvement and gardening supplies', 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Sports & Outdoors', 'Sports equipment and outdoor gear', 4, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- Insert subcategories
INSERT INTO categories (name, description, parent_id, display_order, active, created_at, updated_at, deleted) VALUES
('Men''s Clothing', 'Clothing for men', 1, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Women''s Clothing', 'Clothing for women', 1, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Smartphones', 'Mobile phones and accessories', 2, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Laptops', 'Portable computers', 2, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- Insert sample products
INSERT INTO products (name, description, sku, price, compare_at_price, cost, category_id, weight, weight_unit, active, featured, tags, created_at, updated_at, deleted) VALUES
('Classic White T-Shirt', 'Premium cotton t-shirt with comfortable fit. Perfect for everyday wear.', 'TSHIRT-WHITE-001', 24.99, 34.99, 10.00, 5, 0.2, 'kg', true, true, 'clothing,tshirt,cotton,white', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Blue Denim Jeans', 'Stylish slim-fit denim jeans with modern design.', 'JEANS-BLUE-001', 59.99, 79.99, 25.00, 5, 0.6, 'kg', true, true, 'clothing,jeans,denim,blue', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Red Summer Dress', 'Elegant red dress perfect for summer occasions.', 'DRESS-RED-001', 89.99, 119.99, 35.00, 6, 0.3, 'kg', true, true, 'clothing,dress,summer,red', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Black Leather Jacket', 'Genuine leather jacket with premium quality.', 'JACKET-BLACK-001', 249.99, 349.99, 120.00, 5, 1.2, 'kg', true, false, 'clothing,jacket,leather,black', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Smartphone Pro Max', 'Latest flagship smartphone with advanced features.', 'PHONE-PRO-001', 999.99, 1199.99, 500.00, 7, 0.2, 'kg', true, true, 'electronics,smartphone,mobile', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Wireless Earbuds', 'Premium wireless earbuds with noise cancellation.', 'EARBUDS-001', 149.99, 199.99, 60.00, 7, 0.05, 'kg', true, true, 'electronics,audio,wireless', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Laptop Pro 15"', 'Professional laptop for work and creativity.', 'LAPTOP-PRO-001', 1499.99, 1799.99, 800.00, 8, 2.0, 'kg', true, true, 'electronics,laptop,computer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Sports Running Shoes', 'High-performance running shoes for athletes.', 'SHOES-RUN-001', 129.99, 159.99, 50.00, 4, 0.4, 'kg', true, false, 'sports,shoes,running', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- Insert inventory for products
INSERT INTO inventory (product_id, quantity, reserved_quantity, low_stock_threshold, track_inventory, allow_backorder, created_at, updated_at, deleted) VALUES
(1, 150, 0, 20, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(2, 80, 5, 15, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(3, 45, 2, 10, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(4, 25, 0, 5, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(5, 60, 3, 10, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(6, 200, 10, 30, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(7, 35, 1, 8, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(8, 90, 5, 15, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- Insert initial inventory transactions
INSERT INTO inventory_transactions (product_id, transaction_type, quantity_change, quantity_after, notes, created_at, updated_at, deleted) VALUES
(1, 'PURCHASE', 150, 150, 'Initial stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(2, 'PURCHASE', 80, 80, 'Initial stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(3, 'PURCHASE', 45, 45, 'Initial stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(4, 'PURCHASE', 25, 25, 'Initial stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(5, 'PURCHASE', 60, 60, 'Initial stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(6, 'PURCHASE', 200, 200, 'Initial stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(7, 'PURCHASE', 35, 35, 'Initial stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(8, 'PURCHASE', 90, 90, 'Initial stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
