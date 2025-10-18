# AI Agent API Integration Guide

This document describes the API endpoints specifically designed for AI agent consumption. These endpoints are optimized for simplicity and efficiency.

## Base URL
```
http://localhost:8080/api/agent
```

## Authentication
Currently open for development. In production, use API keys in headers:
```
X-API-Key: your-api-key-here
```

---

## Endpoints

### 1. Health Check
**Purpose**: Verify API connectivity

```http
GET /api/agent/health
```

**Response**:
```json
{
  "success": true,
  "message": null,
  "data": "AI Agent API is running"
}
```

---

### 2. Get All Available Products
**Purpose**: Retrieve complete product catalog for AI processing

**Use Case**: Initial load, cache refresh, answering "What products do you have?"

```http
GET /api/agent/products
```

**Response**:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Classic White T-Shirt",
      "description": "Premium cotton t-shirt with comfortable fit...",
      "sku": "TSHIRT-WHITE-001",
      "price": 24.99,
      "compareAtPrice": 34.99,
      "cost": 10.00,
      "categoryId": 5,
      "categoryName": "Men's Clothing",
      "inventory": {
        "id": 1,
        "productId": 1,
        "quantity": 150,
        "reservedQuantity": 0,
        "availableQuantity": 150,
        "lowStockThreshold": 20,
        "trackInventory": true,
        "allowBackorder": false,
        "inStock": true,
        "lowStock": false,
        "updatedAt": "2025-10-18T10:30:00"
      },
      "images": [
        {
          "id": 1,
          "fileName": "tshirt-white-front.jpg",
          "fileUrl": "/api/files/products/uuid.jpg",
          "contentType": "image/jpeg",
          "fileSize": 245678,
          "altText": "White t-shirt front view",
          "displayOrder": 0,
          "isPrimary": true,
          "createdAt": "2025-10-18T10:00:00"
        }
      ],
      "weight": 0.2,
      "weightUnit": "kg",
      "active": true,
      "featured": true,
      "tags": "clothing,tshirt,cotton,white",
      "createdAt": "2025-10-18T09:00:00",
      "updatedAt": "2025-10-18T09:00:00"
    }
  ]
}
```

**AI Usage Example**:
```python
products = fetch_products()
# Build product knowledge base
for product in products:
    if product['inventory']['inStock']:
        # Add to available products list
        # Train on product descriptions
```

---

### 3. Search Products
**Purpose**: Find products matching customer query

**Use Case**: Customer asks "Do you have red dresses?"

```http
GET /api/agent/products/search?query=red dress
```

**Parameters**:
- `query` (required): Search term for name or description

**Response**: Same structure as Get All Products, filtered results

**AI Usage Example**:
```python
customer_query = "red dress"
results = search_products(customer_query)

if results:
    respond(f"Yes! I found {len(results)} red dresses for you...")
else:
    respond("I'm sorry, we don't have red dresses in stock right now...")
```

---

### 4. Get Product Details by ID
**Purpose**: Fetch detailed information for a specific product

```http
GET /api/agent/products/{id}
```

**Example**: `/api/agent/products/1`

**Response**: Single product object (same structure as above)

---

### 5. Get Product Details by SKU
**Purpose**: Fetch product by unique SKU code

```http
GET /api/agent/products/sku/{sku}
```

**Example**: `/api/agent/products/sku/TSHIRT-WHITE-001`

**Response**: Single product object

---

### 6. Get Products by Category
**Purpose**: Filter products by category

**Use Case**: Customer asks "Show me electronics"

```http
GET /api/agent/products/category/{categoryId}
```

**Example**: `/api/agent/products/category/2`

**Response**: Array of products in that category

---

### 7. Get Featured Products
**Purpose**: Retrieve highlighted/recommended products

**Use Case**: Starting conversation, making recommendations

```http
GET /api/agent/products/featured
```

**Response**: Array of featured products

**AI Usage Example**:
```python
featured = get_featured_products()
respond("Check out our featured products this week:")
for product in featured[:3]:  # Show top 3
    respond(f"- {product['name']}: ${product['price']}")
```

---

### 8. Check Product Inventory
**Purpose**: Verify stock availability for a specific product

**Use Case**: Customer asks "Is this in stock?"

```http
GET /api/agent/inventory/{productId}
```

**Example**: `/api/agent/inventory/1`

**Response**:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "productId": 1,
    "quantity": 150,
    "reservedQuantity": 0,
    "availableQuantity": 150,
    "lowStockThreshold": 20,
    "trackInventory": true,
    "allowBackorder": false,
    "inStock": true,
    "lowStock": false,
    "updatedAt": "2025-10-18T10:30:00"
  }
}
```

**AI Usage Example**:
```python
inventory = check_inventory(product_id)

if inventory['inStock']:
    qty = inventory['availableQuantity']
    if inventory['lowStock']:
        respond(f"Yes, but only {qty} left! Order soon!")
    else:
        respond(f"Yes, we have {qty} units available!")
else:
    if inventory['allowBackorder']:
        respond("Currently out of stock, but you can backorder!")
    else:
        respond("Sorry, this item is currently out of stock.")
```

---

### 9. Get Low Stock Products
**Purpose**: Retrieve products running low on inventory

**Use Case**: Proactive notifications, urgency messaging

```http
GET /api/agent/inventory/low-stock
```

**Response**: Array of products with low stock

**AI Usage Example**:
```python
low_stock = get_low_stock_products()
if customer_viewing_product in low_stock:
    respond("⚠️ Only a few left in stock! Order now!")
```

---

## Common Response Structure

All endpoints return data wrapped in this structure:

```json
{
  "success": true,
  "message": "Optional message",
  "data": {} // or []
}
```

**Error Response**:
```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

---

## Best Practices for AI Integration

### 1. Caching Strategy
```python
# Cache product catalog
products_cache = fetch_all_products()
cache_timestamp = now()

# Refresh every 5 minutes
if now() - cache_timestamp > 5_minutes:
    products_cache = fetch_all_products()
    cache_timestamp = now()
```

### 2. Real-time Inventory Checks
```python
# Don't cache inventory - always check real-time
def process_purchase_intent(product_id):
    inventory = check_inventory(product_id)  # Real-time check
    if inventory['inStock']:
        proceed_with_purchase()
```

### 3. Search Optimization
```python
# Use search for fuzzy matching
customer_query = "blue jens"  # typo
results = search_products(customer_query)  # Will find "Blue Jeans"
```

### 4. Error Handling
```python
try:
    products = fetch_products()
except APIError as e:
    log_error(e)
    respond("I'm having trouble accessing our catalog. Please try again in a moment.")
```

---

## AI Agent Conversation Flow Examples

### Example 1: Product Inquiry
```
Customer: "Do you have any white t-shirts?"

AI Process:
1. Parse intent: searching for "white t-shirts"
2. Call: GET /api/agent/products/search?query=white t-shirt
3. Process results
4. Check inventory for each result
5. Respond with options

AI Response:
"Yes! I found 2 white t-shirts:
1. Classic White T-Shirt - $24.99 (150 in stock)
2. Premium White Tee - $39.99 (45 in stock)
Which one would you like to know more about?"
```

### Example 2: Product Details
```
Customer: "Tell me about the first one"

AI Process:
1. Reference context (product ID from previous search)
2. Call: GET /api/agent/products/1
3. Extract key details

AI Response:
"The Classic White T-Shirt is $24.99 (originally $34.99 - save $10!).
It's made of premium cotton for comfortable everyday wear.
We have 150 units in stock. Would you like to place an order?"
```

### Example 3: Stock Check
```
Customer: "Is product #5 available?"

AI Process:
1. Parse product ID: 5
2. Call: GET /api/agent/inventory/5
3. Check inStock status

AI Response:
"Yes! The Smartphone Pro Max is in stock. We have 60 units available at $999.99."
```

---

## Rate Limiting (Production)

Recommended limits:
- General endpoints: 100 requests/minute
- Search endpoints: 50 requests/minute
- Health check: Unlimited

---

## Support & Questions

For integration support, refer to the main documentation or contact the development team.
