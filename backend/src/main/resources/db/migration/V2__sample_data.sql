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
