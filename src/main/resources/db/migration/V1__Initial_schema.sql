-- V1__Initial_schema.sql
-- Initial database schema for Easify AI Sales Manager

-- Create categories table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    parent_id BIGINT,
    display_order INT,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);

CREATE INDEX idx_category_parent ON categories(parent_id);
CREATE INDEX idx_category_active ON categories(active);

-- Create products table
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    sku VARCHAR(50) NOT NULL UNIQUE,
    price DECIMAL(12, 2) NOT NULL,
    compare_at_price DECIMAL(12, 2),
    cost DECIMAL(12, 2),
    category_id BIGINT,
    weight DECIMAL(10, 2),
    weight_unit VARCHAR(20),
    active BOOLEAN NOT NULL DEFAULT true,
    featured BOOLEAN NOT NULL DEFAULT false,
    tags VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE INDEX idx_product_sku ON products(sku);
CREATE INDEX idx_product_name ON products(name);
CREATE INDEX idx_product_category ON products(category_id);
CREATE INDEX idx_product_active ON products(active);
CREATE INDEX idx_product_featured ON products(featured);

-- Create inventory table
CREATE TABLE inventory (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL UNIQUE,
    quantity INT NOT NULL DEFAULT 0,
    reserved_quantity INT NOT NULL DEFAULT 0,
    low_stock_threshold INT,
    track_inventory BOOLEAN NOT NULL DEFAULT true,
    allow_backorder BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_inventory_product ON inventory(product_id);

-- Create product_images table
CREATE TABLE product_images (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    content_type VARCHAR(100),
    file_size BIGINT,
    alt_text VARCHAR(200),
    display_order INT,
    is_primary BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_product_image_product ON product_images(product_id);

-- Create inventory_transactions table
CREATE TABLE inventory_transactions (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    quantity_change INT NOT NULL,
    quantity_after INT NOT NULL,
    notes VARCHAR(500),
    reference VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_inventory_transaction_product ON inventory_transactions(product_id);
CREATE INDEX idx_inventory_transaction_type ON inventory_transactions(transaction_type);

-- Create platforms table
CREATE TABLE platforms (
    id BIGSERIAL PRIMARY KEY,
    platform_type VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    configuration VARCHAR(500),
    enabled BOOLEAN NOT NULL DEFAULT false,
    webhook_url VARCHAR(500),
    api_key VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT false
);

CREATE INDEX idx_platform_type ON platforms(platform_type);
CREATE INDEX idx_platform_enabled ON platforms(enabled);

-- Create agent_configurations table
CREATE TABLE agent_configurations (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL UNIQUE,
    config_value TEXT,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT false
);

CREATE INDEX idx_agent_config_key ON agent_configurations(config_key);
CREATE INDEX idx_agent_config_active ON agent_configurations(active);

-- Insert default agent configurations
INSERT INTO agent_configurations (config_key, config_value, description, active) VALUES
('agent_name', 'Easify Sales Assistant', 'Name of the AI sales agent', true),
('agent_greeting', 'Hello! I''m here to help you find the perfect products. How can I assist you today?', 'Default greeting message', true),
('agent_language', 'en', 'Default language for the agent', true),
('max_response_length', '500', 'Maximum length of agent responses in characters', true),
('enable_product_recommendations', 'true', 'Enable AI product recommendations', true);

-- Insert sample platforms
INSERT INTO platforms (platform_type, name, enabled) VALUES
('TELEGRAM', 'Telegram Bot', false),
('INSTAGRAM', 'Instagram Direct', false),
('WHATSAPP', 'WhatsApp Business', false),
('WEB_CHAT', 'Web Chat Widget', true);

COMMENT ON TABLE categories IS 'Product categories with hierarchical support';
COMMENT ON TABLE products IS 'Product catalog with pricing and details';
COMMENT ON TABLE inventory IS 'Product inventory and stock levels';
COMMENT ON TABLE product_images IS 'Product images stored in filesystem';
COMMENT ON TABLE inventory_transactions IS 'Audit trail for inventory changes';
COMMENT ON TABLE platforms IS 'Messaging platforms integrated with AI agent';
COMMENT ON TABLE agent_configurations IS 'AI agent configuration settings';
