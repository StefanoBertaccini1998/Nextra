# ADR-0003 – Database Initialization and Sample Data

**Status:** Accepted
**Date:** 2025-10-14
**Version:** V1 (Initial schema) + V2 (Sample data)
**Owner:** @stefano.bertaccini

---

## Context

To support the ERP backend for **Nextra**, a relational schema was required to represent
the production workflow of handcrafted sofas, including models, variants, frames, feet,
and order management.

The database is implemented in **PostgreSQL 16** and managed through **Flyway** migrations.
Each table supports a flexible `custom_fields JSONB` column for future extensibility.

---

## Decision

Two Flyway migrations are defined:

### 🧱 V1 — `V1__init_schema.sql`
Creates the foundational schema:
- `sofa_frame` → Defines sofa structures (materials, base sizes)
- `feet` → Defines feet models (height, material, style)
- `article` → Represents the main sofa model (base model)
- `variant` → Defines color, fabric, and optional configurations per article
- `"order"` → Tracks customer orders
- `order_item` → Connects variants to orders

### 🧩 V2 — `V2__sample_data.sql`
Provides realistic seed data for development, UI integration and API testing:
- Two sofa frames
- Three feet models
- Three articles with multiple variants
- One example order (Mario Rossi)

---

## Schema Overview (ER Diagram)

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

## Flyway Migration Files

### 📜 V1__init_schema.sql

```sql
CREATE TABLE sofa_frame (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    material VARCHAR(100),
    custom_fields JSONB DEFAULT '{}'::jsonb
);

CREATE TABLE feet (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    material VARCHAR(100),
    height_cm DECIMAL(5,2),
    custom_fields JSONB DEFAULT '{}'::jsonb
);

CREATE TABLE article (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL,
    base_price NUMERIC(10,2) NOT NULL,
    frame_id INT REFERENCES sofa_frame(id),
    feet_id INT REFERENCES feet(id),
    custom_fields JSONB DEFAULT '{}'::jsonb
);

CREATE TABLE variant (
    id SERIAL PRIMARY KEY,
    article_id INT REFERENCES article(id) ON DELETE CASCADE,
    color VARCHAR(50),
    fabric VARCHAR(100),
    optional JSONB DEFAULT '{}'::jsonb,
    price_delta NUMERIC(10,2) DEFAULT 0,
    custom_fields JSONB DEFAULT '{}'::jsonb
);

CREATE TABLE "order" (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(150),
    status VARCHAR(50) DEFAULT 'DRAFT',
    total NUMERIC(10,2) DEFAULT 0,
    custom_fields JSONB DEFAULT '{}'::jsonb
);

CREATE TABLE order_item (
    id SERIAL PRIMARY KEY,
    order_id INT REFERENCES "order"(id) ON DELETE CASCADE,
    variant_id INT REFERENCES variant(id),
    quantity INT NOT NULL DEFAULT 1,
    unit_price NUMERIC(10,2),
    subtotal NUMERIC(10,2),
    custom_fields JSONB DEFAULT '{}'::jsonb
);
```

---

### 📜 V2__sample_data.sql

```sql
-- Insert sofa frames
INSERT INTO sofa_frame (name, material, custom_fields)
VALUES
('Compact Frame', 'Wood', '{"width": 180, "depth": 85, "notes": "Small model"}'),
('Deluxe Frame', 'Steel', '{"width": 210, "depth": 90, "notes": "Premium structure"}');

-- Insert feet
INSERT INTO feet (name, material, height_cm, custom_fields)
VALUES
('Legno Naturale', 'Oak', 12.5, '{"finish": "matte"}'),
('Metallo Nero', 'Iron', 10.0, '{"finish": "glossy"}'),
('Cromo Lucido', 'Steel', 9.5, '{"finish": "mirror"}');

-- Insert articles
INSERT INTO article (code, name, base_price, frame_id, feet_id, custom_fields)
VALUES
('A001', 'Sofia 2 posti', 950.00, 1, 1, '{"description": "Divano compatto per piccoli spazi"}'),
('A002', 'Milano 3 posti', 1250.00, 2, 2, '{"description": "Classico con linee moderne"}'),
('A003', 'Lounge Deluxe', 1850.00, 2, 3, '{"description": "Premium con chaise longue"}');

-- Insert variants
INSERT INTO variant (article_id, color, fabric, optional, price_delta, custom_fields)
VALUES
(1, 'Beige', 'Lino', '{"piping":"contrast"}', 0, '{}'),
(1, 'Blu notte', 'Velluto', '{"piping":"tonal"}', 150, '{}'),
(2, 'Grigio chiaro', 'Cotone', '{"stitching":"visible"}', 0, '{}'),
(3, 'Verde oliva', 'Pelle', '{"armrest":"wide"}', 300, '{}');

-- Insert order
INSERT INTO "order" (customer_name, status, total, custom_fields)
VALUES ('Mario Rossi', 'IN_PROGRESS', 2400.00, '{"priority": "high"}');

-- Insert order items
INSERT INTO order_item (order_id, variant_id, quantity, unit_price, subtotal, custom_fields)
VALUES
(1, 1, 1, 950.00, 950.00, '{}'),
(1, 4, 1, 1450.00, 1450.00, '{}');
```

---

## How to Run

```bash
# Clean and recreate database
./gradlew flywayClean flywayMigrate

# Run only migrations
./gradlew flywayMigrate
```

---

## Verification Commands

```bash
docker exec -it nextra-postgres psql -U nextra -d nextra -c "\dt"
docker exec -it nextra-postgres psql -U nextra -d nextra -c "SELECT * FROM article;"
```

Expected output (simplified):

| id | code | name           | base_price |
| -- | ---- | -------------- | ---------- |
| 1  | A001 | Sofia 2 posti  | 950.00     |
| 2  | A002 | Milano 3 posti | 1250.00    |
| 3  | A003 | Lounge Deluxe  | 1850.00    |

---
