# ERD – Nextra Database Schema

**System:** Nextra ERP
**Scope:** Sofa production and order management
**Database:** PostgreSQL 16
**Version:** 1.0
**Maintainer:** @stefano.bertaccini

---

## 1. Overview

The **Nextra ERP** database represents the production flow of handcrafted sofas.
It manages models, structural elements (frames, feet), variants (fabrics, colors, options), and customer orders.

Designed with:
- **PostgreSQL 16**
- **Flyway** for migration versioning
- **JSONB** fields for extensibility
- **Foreign keys with ON DELETE CASCADE** for relational integrity
- **Normalization level:** 3NF with flexible semi-structured extensions

---

## 2. Entity Relationship Diagram

```mermaid
erDiagram
    SOFA_FRAME {
        int id PK
        varchar name
        varchar material
        jsonb custom_fields
    }

    FEET {
        int id PK
        varchar name
        varchar material
        decimal height_cm
        jsonb custom_fields
    }

    ARTICLE {
        int id PK
        varchar code
        varchar name
        numeric base_price
        int frame_id FK
        int feet_id FK
        jsonb custom_fields
    }

    VARIANT {
        int id PK
        int article_id FK
        varchar color
        varchar fabric
        jsonb optional
        numeric price_delta
        jsonb custom_fields
    }

    ORDER {
        int id PK
        varchar customer_name
        varchar status
        numeric total
        jsonb custom_fields
    }

    ORDER_ITEM {
        int id PK
        int order_id FK
        int variant_id FK
        int quantity
        numeric unit_price
        numeric subtotal
        jsonb custom_fields
    }

    SOFA_FRAME ||--o{ ARTICLE : "has"
    FEET ||--o{ ARTICLE : "has"
    ARTICLE ||--o{ VARIANT : "defines"
    VARIANT ||--o{ ORDER_ITEM : "used in"
    ORDER ||--o{ ORDER_ITEM : "contains"
````

---

## 3. Tables Description

### 🧱 `sofa_frame`

| Column          | Type         | Description                                   |
| --------------- | ------------ | --------------------------------------------- |
| `id`            | SERIAL PK    | Primary key                                   |
| `name`          | VARCHAR(100) | Frame name                                    |
| `material`      | VARCHAR(100) | Material type (wood, steel...)                |
| `custom_fields` | JSONB        | Optional metadata (dimensions, density, etc.) |

---

### 🦶 `feet`

| Column          | Type         | Description                             |
| --------------- | ------------ | --------------------------------------- |
| `id`            | SERIAL PK    | Primary key                             |
| `name`          | VARCHAR(100) | Feet name                               |
| `material`      | VARCHAR(100) | Material (iron, oak, chrome...)         |
| `height_cm`     | DECIMAL(5,2) | Height in centimeters                   |
| `custom_fields` | JSONB        | Custom attributes (finish, color, etc.) |

---

### 🛋️ `article`

| Column          | Type                    | Description                                    |
| --------------- | ----------------------- | ---------------------------------------------- |
| `id`            | SERIAL PK               | Primary key                                    |
| `code`          | VARCHAR(50)             | Unique code (SKU)                              |
| `name`          | VARCHAR(150)            | Sofa name or model                             |
| `base_price`    | NUMERIC(10,2)           | Default base price                             |
| `frame_id`      | INT FK → sofa_frame(id) | Associated frame                               |
| `feet_id`       | INT FK → feet(id)       | Associated feet                                |
| `custom_fields` | JSONB                   | Additional details (marketing, designer, etc.) |

---

### 🎨 `variant`

| Column          | Type                 | Description                                       |
| --------------- | -------------------- | ------------------------------------------------- |
| `id`            | SERIAL PK            | Primary key                                       |
| `article_id`    | INT FK → article(id) | Related article                                   |
| `color`         | VARCHAR(50)          | Variant color                                     |
| `fabric`        | VARCHAR(100)         | Fabric name                                       |
| `optional`      | JSONB                | Optional configurations (piping, stitching, etc.) |
| `price_delta`   | NUMERIC(10,2)        | Additional price on top of base                   |
| `custom_fields` | JSONB                | Notes or manufacturing details                    |

---

### 📦 `"order"`

| Column          | Type          | Description                                           |
| --------------- | ------------- | ----------------------------------------------------- |
| `id`            | SERIAL PK     | Primary key                                           |
| `customer_name` | VARCHAR(150)  | Customer full name                                    |
| `status`        | VARCHAR(50)   | Workflow status (DRAFT, IN_PROGRESS, DELIVERED, etc.) |
| `total`         | NUMERIC(10,2) | Total price of the order                              |
| `custom_fields` | JSONB         | Any other information (priority, discount codes...)   |

---

### 🧾 `order_item`

| Column          | Type                 | Description          |
| --------------- | -------------------- | -------------------- |
| `id`            | SERIAL PK            | Primary key          |
| `order_id`      | INT FK → order(id)   | Associated order     |
| `variant_id`    | INT FK → variant(id) | Product variant      |
| `quantity`      | INT                  | Quantity ordered     |
| `unit_price`    | NUMERIC(10,2)        | Price per unit       |
| `subtotal`      | NUMERIC(10,2)        | Computed subtotal    |
| `custom_fields` | JSONB                | Notes or adjustments |

---

## 4. Relationships Summary

| Source       | Relationship | Target       | Type           |
| ------------ | ------------ | ------------ | -------------- |
| `sofa_frame` | 1 → N        | `article`    | Composition    |
| `feet`       | 1 → N        | `article`    | Composition    |
| `article`    | 1 → N        | `variant`    | Specialization |
| `variant`    | 1 → N        | `order_item` | Inclusion      |
| `order`      | 1 → N        | `order_item` | Aggregation    |

---

## 5. Design Principles

* **JSONB Extensibility:**
  Each entity includes `custom_fields` to support additional metadata
  without schema modifications (e.g., production tags, material grades, etc.)

* **Normalization and Flexibility:**
  3NF core structure with optional denormalization for order performance.

* **Cascade Deletion:**

  * Deleting an `article` removes its `variants`
  * Deleting an `order` removes its `order_items`

* **Indexes & Constraints:**

  * Unique index on `article.code`
  * Foreign keys validated with referential integrity

---

## 6. Future Enhancements

| Area               | Description                                  | Priority |
| ------------------ | -------------------------------------------- | -------- |
| Inventory tracking | Add `stock`, `warehouse_location` tables     | Medium   |
| Order history      | Implement audit tables for versioning        | Medium   |
| Pricing engine     | Add table for discounts, promotions          | High     |
| User management    | Integrate with Keycloak (roles, permissions) | High     |

---
