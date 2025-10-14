CREATE TABLE sofa_frame (
    id BIGSERIAL   PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    material VARCHAR(100),
    custom_fields JSONB DEFAULT '{}'::jsonb
);

CREATE TABLE feet (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    material VARCHAR(100),
    height_cm DOUBLE PRECISION,
    custom_fields JSONB DEFAULT '{}'::jsonb
);

CREATE TABLE article (
    id BIGSERIAL    PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL,
    base_price NUMERIC(10,2) NOT NULL,
    frame_id BIGINT REFERENCES sofa_frame(id),
    feet_id BIGINT REFERENCES feet(id),
    custom_fields JSONB DEFAULT '{}'::jsonb
);

CREATE TABLE variant (
    id BIGSERIAL    PRIMARY KEY,
    article_id BIGINT REFERENCES article(id) ON DELETE CASCADE,
    color VARCHAR(50),
    fabric VARCHAR(100),
    optional JSONB DEFAULT '{}'::jsonb,
    price_delta NUMERIC(10,2) DEFAULT 0,
    custom_fields JSONB DEFAULT '{}'::jsonb
);

CREATE TABLE "order" (
    id BIGSERIAL    PRIMARY KEY,
    customer_name VARCHAR(150),
    status VARCHAR(50) DEFAULT 'DRAFT',
    total NUMERIC(10,2) DEFAULT 0,
    custom_fields JSONB DEFAULT '{}'::jsonb
);

CREATE TABLE order_item (
    id BIGSERIAL    PRIMARY KEY,
    order_id BIGINT REFERENCES "order"(id) ON DELETE CASCADE,
    variant_id BIGINT REFERENCES variant(id),
    quantity INT NOT NULL DEFAULT 1,
    unit_price NUMERIC(10,2),
    subtotal NUMERIC(10,2),
    custom_fields JSONB DEFAULT '{}'::jsonb
);
