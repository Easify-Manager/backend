```````````````````````````````# Easify Backend - Complete API Guide

**Version:** 1.0  
**Base URL:** `http://localhost:8080`  
**Last Updated:** October 18, 2025

---

## Table of Contents

1. [Overview](#overview)
2. [Authentication](#authentication)
3. [Common Response Structure](#common-response-structure)
4. [Error Handling](#error-handling)
5. [Pagination & Sorting](#pagination--sorting)
6. [Product Management API](#product-management-api)
7. [AI Agent API](#ai-agent-api)
8. [Request/Response Examples](#requestresponse-examples)

---

## Overview

This API provides comprehensive product management and AI agent integration capabilities. The API is divided into two main sections:

- **Product Management API** (`/api/products`): Full CRUD operations for products
- **AI Agent API** (`/api/agent`): Simplified, read-only endpoints optimized for AI agent consumption

---

## Authentication

**Development:** Currently open for development purposes.

**Production:** Use API keys in request headers:
```http
X-API-Key: your-api-key-here
```

---

## Common Response Structure

All endpoints return data wrapped in a standard response structure:

### Success Response
```json
{
  "success": true,
  "message": "Optional success message",
  "data": {} // or []
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "errors": [
    {
      "field": "fieldName",
      "message": "Validation error message"
    }
  ]
}
```

### HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request (validation errors) |
| 404 | Resource Not Found |
| 409 | Conflict (duplicate resource) |
| 500 | Internal Server Error |

---

## Error Handling

### Common Error Scenarios

**Resource Not Found (404)**
```json
{
  "success": false,
  "message": "Product not found with ID: 123",
  "data": null
}
```

**Validation Error (400)**
```json
{
  "success": false,
  "message": "Validation failed",
  "data": null,
  "errors": [
    {
      "field": "name",
      "message": "Product name is required"
    },
    {
      "field": "price",
      "message": "Price must be greater than 0"
    }
  ]
}
```

**Duplicate Resource (409)**
```json
{
  "success": false,
  "message": "Product with SKU 'TSHIRT-001' already exists",
  "data": null
}
```

---

## Pagination & Sorting

Endpoints that return lists support pagination and sorting using Spring Data parameters:

### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `page` | integer | 0 | Page number (0-based) |
| `size` | integer | 20 | Number of items per page |
| `sort` | string | varies | Sort field and direction |

### Examples

```http
# Get page 2 with 50 items
GET /api/products?page=1&size=50

# Sort by price ascending
GET /api/products?sort=price,asc

# Sort by multiple fields
GET /api/products?sort=price,asc&sort=name,desc

# Combine pagination and sorting
GET /api/products?page=0&size=20&sort=createdAt,desc
```

### Paginated Response Structure

```json
{
  "success": true,
  "data": {
    "content": [...],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": { "sorted": true },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 5,
    "totalElements": 98,
    "last": false,
    "first": true,
    "numberOfElements": 20,
    "size": 20,
    "number": 0,
    "empty": false
  }
}
```

---

## Product Management API

Base path: `/api/products`

### 1. Create Product

**Endpoint:** `POST /api/products`

**Description:** Create a new product with inventory information.

**Request Body:**
```json
{
  "name": "Classic White T-Shirt",
  "description": "Premium cotton t-shirt with comfortable fit",
  "sku": "TSHIRT-WHITE-001",
  "price": 24.99,
  "compareAtPrice": 34.99,
  "cost": 10.00,
  "categoryId": 5,
  "quantity": 150,
  "lowStockThreshold": 20,
  "trackInventory": true,
  "allowBackorder": false,
  "weight": 0.2,
  "weightUnit": "kg",
  "active": true,
  "featured": true,
  "tags": "clothing,tshirt,cotton,white"
}
```

**Validation Rules:**
- `name`: Required, 2-200 characters
- `sku`: Required, unique, max 50 characters
- `price`: Required, must be > 0
- `compareAtPrice`: Optional, must be >= 0
- `cost`: Optional, must be >= 0

**Success Response:** `201 Created`
```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 1,
    "name": "Classic White T-Shirt",
    "description": "Premium cotton t-shirt with comfortable fit",
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
      "lowStock": false
    },
    "images": [],
    "weight": 0.2,
    "weightUnit": "kg",
    "active": true,
    "featured": true,
    "tags": "clothing,tshirt,cotton,white",
    "createdAt": "2025-10-18T10:00:00",
    "updatedAt": "2025-10-18T10:00:00"
  }
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Classic White T-Shirt",
    "sku": "TSHIRT-WHITE-001",
    "price": 24.99,
    "quantity": 150
  }'
```

---

### 2. Update Product

**Endpoint:** `PUT /api/products/{id}`

**Description:** Update an existing product.

**Path Parameters:**
- `id` (Long): Product ID

**Request Body:** Same as Create Product

**Success Response:** `200 OK`
```json
{
  "success": true,
  "message": "Product updated successfully",
  "data": { /* Product object */ }
}
```

**cURL Example:**
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Classic White T-Shirt - Updated",
    "sku": "TSHIRT-WHITE-001",
    "price": 29.99,
    "quantity": 200
  }'
```

---

### 3. Get Product by ID

**Endpoint:** `GET /api/products/{id}`

**Description:** Retrieve a single product by its ID.

**Path Parameters:**
- `id` (Long): Product ID

**Success Response:** `200 OK`
```json
{
  "success": true,
  "data": { /* Product object */ }
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/products/1
```

---

### 4. Get Product by SKU

**Endpoint:** `GET /api/products/sku/{sku}`

**Description:** Retrieve a product by its unique SKU code.

**Path Parameters:**
- `sku` (String): Product SKU

**Success Response:** `200 OK`

**cURL Example:**
```bash
curl http://localhost:8080/api/products/sku/TSHIRT-WHITE-001
```

---

### 5. Get All Products (Paginated)

**Endpoint:** `GET /api/products`

**Description:** Retrieve all products with pagination and sorting.

**Query Parameters:**
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)
- `sort` (string, optional): Sort criteria (default: createdAt,desc)

**Success Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [ /* Array of Product objects */ ],
    "totalPages": 5,
    "totalElements": 98,
    "size": 20,
    "number": 0
  }
}
```

**cURL Examples:**
```bash
# Default pagination
curl http://localhost:8080/api/products

# Custom page and size
curl "http://localhost:8080/api/products?page=1&size=50"

# Sorted by price
curl "http://localhost:8080/api/products?sort=price,asc"
```

---

### 6. Get Active Products

**Endpoint:** `GET /api/products/active`

**Description:** Retrieve all active (non-deleted, active=true) products.

**Success Response:** `200 OK`
```json
{
  "success": true,
  "data": [ /* Array of active Product objects */ ]
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/products/active
```

---

### 7. Get Products by Category

**Endpoint:** `GET /api/products/category/{categoryId}`

**Description:** Retrieve all products belonging to a specific category.

**Path Parameters:**
- `categoryId` (Long): Category ID

**Success Response:** `200 OK`

**cURL Example:**
```bash
curl http://localhost:8080/api/products/category/5
```

---

### 8. Get Featured Products

**Endpoint:** `GET /api/products/featured`

**Description:** Retrieve products marked as featured.

**Success Response:** `200 OK`

**cURL Example:**
```bash
curl http://localhost:8080/api/products/featured
```

---

### 9. Search Products

**Endpoint:** `GET /api/products/search`

**Description:** Search products by name or description with optional price filtering.

**Query Parameters:**
- `query` (string, required): Search term
- `minPrice` (decimal, optional): Minimum price filter
- `maxPrice` (decimal, optional): Maximum price filter
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Success Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [ /* Matching products */ ],
    "totalElements": 15,
    "totalPages": 1,
    "size": 20,
    "number": 0
  }
}
```

**cURL Examples:**
```bash
# Basic search
curl "http://localhost:8080/api/products/search?query=shirt"

# Search with price range
curl "http://localhost:8080/api/products/search?query=shirt&minPrice=10&maxPrice=50"

# Search with pagination
curl "http://localhost:8080/api/products/search?query=shirt&page=0&size=10"
```

---

### 10. Get Low Stock Products

**Endpoint:** `GET /api/products/low-stock`

**Description:** Retrieve products with inventory at or below their low stock threshold.

**Success Response:** `200 OK`

**cURL Example:**
```bash
curl http://localhost:8080/api/products/low-stock
```

---

### 11. Get Out of Stock Products

**Endpoint:** `GET /api/products/out-of-stock`

**Description:** Retrieve products with zero available inventory.

**Success Response:** `200 OK`

**cURL Example:**
```bash
curl http://localhost:8080/api/products/out-of-stock
```

---

### 12. Add Product Image

**Endpoint:** `POST /api/products/{id}/images`

**Description:** Upload and attach an image to a product.

**Path Parameters:**
- `id` (Long): Product ID

**Request:** `multipart/form-data`

**Form Parameters:**
- `file` (file, required): Image file
- `altText` (string, optional): Alt text for image
- `isPrimary` (boolean, optional): Mark as primary image (default: false)

**Success Response:** `200 OK`
```json
{
  "success": true,
  "message": "Image added successfully",
  "data": { /* Product object with new image */ }
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/products/1/images \
  -F "file=@/path/to/image.jpg" \
  -F "altText=Product front view" \
  -F "isPrimary=true"
```

---

### 13. Delete Product Image

**Endpoint:** `DELETE /api/products/{productId}/images/{imageId}`

**Description:** Remove an image from a product.

**Path Parameters:**
- `productId` (Long): Product ID
- `imageId` (Long): Image ID

**Success Response:** `200 OK`
```json
{
  "success": true,
  "message": "Image deleted successfully",
  "data": null
}
```

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/products/1/images/5
```

---

### 14. Delete Product

**Endpoint:** `DELETE /api/products/{id}`

**Description:** Soft delete a product (marks as deleted, doesn't remove from database).

**Path Parameters:**
- `id` (Long): Product ID

**Success Response:** `200 OK`
```json
{
  "success": true,
  "message": "Product deleted successfully",
  "data": null
}
```

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

---

## AI Agent API

Base path: `/api/agent`

**Purpose:** Simplified, read-only endpoints optimized for AI agent consumption.

### 1. Health Check

**Endpoint:** `GET /api/agent/health`

**Description:** Verify API connectivity and status.

**Success Response:** `200 OK`
```json
{
  "success": true,
  "data": "AI Agent API is running"
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/agent/health
```

---

### 2. Get Available Products

**Endpoint:** `GET /api/agent/products`

**Description:** Retrieve all active products for AI processing. This is the primary endpoint for building product catalog knowledge.

**Use Cases:**
- Initial product catalog load
- Cache refresh
- Answering "What products do you have?"

**Success Response:** `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Classic White T-Shirt",
      "description": "Premium cotton t-shirt",
      "sku": "TSHIRT-WHITE-001",
      "price": 24.99,
      "compareAtPrice": 34.99,
      "inventory": {
        "availableQuantity": 150,
        "inStock": true,
        "lowStock": false
      },
      "images": [...]
    }
  ]
}
```

**AI Integration Example:**
```python
# Build product knowledge base
products = requests.get("http://localhost:8080/api/agent/products").json()
for product in products['data']:
    if product['inventory']['inStock']:
        add_to_knowledge_base(product)
```

**cURL Example:**
```bash
curl http://localhost:8080/api/agent/products
```

---

### 3. Get Product Details by ID

**Endpoint:** `GET /api/agent/products/{id}`

**Description:** Retrieve detailed information for a specific product.

**Path Parameters:**
- `id` (Long): Product ID

**Success Response:** `200 OK`

**cURL Example:**
```bash
curl http://localhost:8080/api/agent/products/1
```

---

### 4. Get Product Details by SKU

**Endpoint:** `GET /api/agent/products/sku/{sku}`

**Description:** Retrieve product information by SKU code.

**Path Parameters:**
- `sku` (String): Product SKU

**Success Response:** `200 OK`

**cURL Example:**
```bash
curl http://localhost:8080/api/agent/products/sku/TSHIRT-WHITE-001
```

---

### 5. Search Products

**Endpoint:** `GET /api/agent/products/search`

**Description:** Find products matching a search query with optional price filtering.

**Query Parameters:**
- `query` (string, required): Search term
- `minPrice` (decimal, optional): Minimum price
- `maxPrice` (decimal, optional): Maximum price
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Use Cases:**
- Customer asks "Do you have red dresses?"
- "Show me laptops under $1000"

**Success Response:** `200 OK`
```json
{
  "success": true,
  "data": [ /* Array of matching products */ ]
}
```

**AI Integration Example:**
```python
# Customer query: "Do you have red dresses under $100?"
params = {
    "query": "red dress",
    "maxPrice": 100,
    "size": 10
}
results = requests.get(
    "http://localhost:8080/api/agent/products/search",
    params=params
).json()

if results['data']:
    respond(f"Yes! I found {len(results['data'])} red dresses under $100...")
else:
    respond("I'm sorry, we don't have red dresses in that price range...")
```

**cURL Examples:**
```bash
# Basic search
curl "http://localhost:8080/api/agent/products/search?query=shirt"

# Search with price filter
curl "http://localhost:8080/api/agent/products/search?query=laptop&minPrice=500&maxPrice=1500"
```

---

### 6. Get Products by Category

**Endpoint:** `GET /api/agent/products/category/{categoryId}`

**Description:** Retrieve all products in a specific category.

**Path Parameters:**
- `categoryId` (Long): Category ID

**Use Case:** Customer asks "Show me electronics"

**Success Response:** `200 OK`

**cURL Example:**
```bash
curl http://localhost:8080/api/agent/products/category/2
```

---

### 7. Get Featured Products

**Endpoint:** `GET /api/agent/products/featured`

**Description:** Retrieve featured/recommended products.

**Use Cases:**
- Starting a conversation
- Making recommendations
- "What's popular?"

**Success Response:** `200 OK`

**AI Integration Example:**
```python
featured = requests.get("http://localhost:8080/api/agent/products/featured").json()
respond("Check out our featured products this week:")
for product in featured['data'][:3]:
    respond(f"- {product['name']}: ${product['price']}")
```

**cURL Example:**
```bash
curl http://localhost:8080/api/agent/products/featured
```

---

### 8. Check Product Inventory

**Endpoint:** `GET /api/agent/inventory/{productId}`

**Description:** Check real-time stock availability for a specific product.

**Path Parameters:**
- `productId` (Long): Product ID

**Use Case:** Customer asks "Is this in stock?"

**Success Response:** `200 OK`
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

**AI Integration Example:**
```python
inventory = requests.get(f"http://localhost:8080/api/agent/inventory/{product_id}").json()['data']

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

**cURL Example:**
```bash
curl http://localhost:8080/api/agent/inventory/1
```

---

### 9. Get Low Stock Products

**Endpoint:** `GET /api/agent/inventory/low-stock`

**Description:** Retrieve products running low on inventory.

**Use Cases:**
- Proactive notifications
- Urgency messaging
- "Limited stock" alerts

**Success Response:** `200 OK`

**AI Integration Example:**
```python
low_stock = requests.get("http://localhost:8080/api/agent/inventory/low-stock").json()['data']
low_stock_ids = [p['id'] for p in low_stock]

if customer_viewing_product_id in low_stock_ids:
    respond("⚠️ Only a few left in stock! Order now!")
```

**cURL Example:**
```bash
curl http://localhost:8080/api/agent/inventory/low-stock
```

---

## Request/Response Examples

### Complete Product Object

```json
{
  "id": 1,
  "name": "Classic White T-Shirt",
  "description": "Premium cotton t-shirt with comfortable fit. Perfect for everyday wear.",
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
    "reservedQuantity": 5,
    "availableQuantity": 145,
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
      "fileUrl": "/api/files/products/550e8400-e29b-41d4-a716-446655440000.jpg",
      "contentType": "image/jpeg",
      "fileSize": 245678,
      "altText": "White t-shirt front view",
      "displayOrder": 0,
      "isPrimary": true,
      "createdAt": "2025-10-18T10:00:00"
    },
    {
      "id": 2,
      "fileName": "tshirt-white-back.jpg",
      "fileUrl": "/api/files/products/550e8400-e29b-41d4-a716-446655440001.jpg",
      "contentType": "image/jpeg",
      "fileSize": 238901,
      "altText": "White t-shirt back view",
      "displayOrder": 1,
      "isPrimary": false,
      "createdAt": "2025-10-18T10:01:00"
    }
  ],
  "weight": 0.2,
  "weightUnit": "kg",
  "active": true,
  "featured": true,
  "tags": "clothing,tshirt,cotton,white,mens",
  "createdAt": "2025-10-18T09:00:00",
  "updatedAt": "2025-10-18T09:00:00"
}
```

---

## AI Agent Integration Best Practices

### 1. Caching Strategy

```python
import time

class ProductCache:
    def __init__(self):
        self.products = []
        self.last_refresh = 0
        self.cache_ttl = 300  # 5 minutes
    
    def get_products(self):
        if time.time() - self.last_refresh > self.cache_ttl:
            self.products = fetch_all_products()
            self.last_refresh = time.time()
        return self.products
```

### 2. Real-time Inventory Checks

```python
# DON'T cache inventory - always check real-time
def process_purchase_intent(product_id):
    inventory = requests.get(
        f"http://localhost:8080/api/agent/inventory/{product_id}"
    ).json()['data']
    
    if inventory['inStock'] and inventory['availableQuantity'] > 0:
        proceed_with_purchase()
    else:
        handle_out_of_stock()
```

### 3. Error Handling

```python
def safe_api_call(url, params=None):
    try:
        response = requests.get(url, params=params, timeout=5)
        response.raise_for_status()
        return response.json()
    except requests.Timeout:
        log_error("API timeout")
        return {"success": False, "message": "Service temporarily unavailable"}
    except requests.RequestException as e:
        log_error(f"API error: {e}")
        return {"success": False, "message": "Unable to fetch data"}
```

### 4. Conversation Flow Example

```python
# Customer: "Do you have any white t-shirts under $30?"

# Step 1: Search with filters
results = requests.get(
    "http://localhost:8080/api/agent/products/search",
    params={"query": "white t-shirt", "maxPrice": 30}
).json()

if not results['data']:
    respond("I don't have white t-shirts under $30, but I can show you similar options.")
    # Retry without price filter
    results = requests.get(
        "http://localhost:8080/api/agent/products/search",
        params={"query": "white t-shirt"}
    ).json()

# Step 2: Check inventory for each result
available_products = []
for product in results['data']:
    inventory = requests.get(
        f"http://localhost:8080/api/agent/inventory/{product['id']}"
    ).json()['data']
    
    if inventory['inStock']:
        available_products.append({
            **product,
            'availableQty': inventory['availableQuantity']
        })

# Step 3: Present options
if available_products:
    respond(f"Yes! I found {len(available_products)} white t-shirts under $30:")
    for p in available_products[:3]:  # Show top 3
        respond(f"- {p['name']}: ${p['price']} ({p['availableQty']} in stock)")
```

---

## Rate Limiting (Production)

Recommended limits for production environments:

| Endpoint Type | Limit | Window |
|--------------|-------|--------|
| General endpoints | 100 requests | per minute |
| Search endpoints | 50 requests | per minute |
| Image upload | 10 requests | per minute |
| Health check | Unlimited | - |

---

## Postman Collection

Import this collection to test all endpoints:

```json
{
  "info": {
    "name": "Easify Backend API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Products",
      "item": [
        {
          "name": "Get All Products",
          "request": {
            "method": "GET",
            "url": "{{baseUrl}}/api/products"
          }
        }
      ]
    }
  ],
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080"
    }
  ]
}
```

---

## Support & Questions

For API support or questions:
- **Documentation:** This guide
- **Source Code:** `src/main/java/uz/easify/backend/controller/`
- **Issues:** Report bugs via your issue tracking system

---

**Last Updated:** October 18, 2025  
**API Version:** 1.0

