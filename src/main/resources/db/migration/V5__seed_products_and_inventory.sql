-- ============================================================================
-- 7. SEED STOREFRONT PRODUCTS & INITIAL INVENTORY
-- ============================================================================

-- 1. Insert Products
INSERT INTO products (id, sku, name, description, price, currency, status, category_id, is_featured, image_url, created_at, updated_at)
VALUES
(
    'a0000000-0000-0000-0000-000000000101', 'FF-101', 'The Everyday Tee',
    'A soft cotton crewneck with a relaxed shoulder and an easy straight hem. Wear it tucked under tailoring or loose over denim.',
    38.00, 'MAD', 'PUBLISHED', 'tops', TRUE,
    'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
),
(
    'a0000000-0000-0000-0000-000000000201', 'FF-201', 'The Field Jacket',
    'A structured everyday layer with an easy fit through the body. Roomy enough for a light knit underneath.',
    128.00, 'MAD', 'PUBLISHED', 'layers', TRUE,
    'https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
),
(
    'a0000000-0000-0000-0000-000000000301', 'FF-301', 'The Straight Jean',
    'Classic straight-leg denim with a mid-rise waist and a lived-in wash. The rigid cotton softens with wear.',
    94.00, 'MAD', 'PUBLISHED', 'bottoms', TRUE,
    'https://images.unsplash.com/photo-1542272604-787c3835535d?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
),
(
    'a0000000-0000-0000-0000-000000000401', 'FF-401', 'The Day Tote',
    'A roomy carryall with a comfortable handle drop and space for the things you take everywhere.',
    68.00, 'MAD', 'PUBLISHED', 'accessories', TRUE,
    'https://images.unsplash.com/photo-1544816155-12df9643f363?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
),
(
    'a0000000-0000-0000-0000-000000000102', 'FF-102', 'The Studio Shirt',
    'A crisp button-through shirt with a curved hem and generous sleeves you can roll back.',
    72.00, 'MAD', 'PUBLISHED', 'tops', FALSE,
    'https://images.unsplash.com/photo-1598033129183-c4f50c736f10?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
),
(
    'a0000000-0000-0000-0000-000000000202', 'FF-202', 'The Soft Knit',
    'An easy midweight knit with soft edges and a comfortable neckline for days between seasons.',
    89.00, 'MAD', 'PUBLISHED', 'layers', FALSE,
    'https://images.unsplash.com/photo-1576566588028-4147f3842f27?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
),
(
    'a0000000-0000-0000-0000-000000000302', 'FF-302', 'The Weekend Denim',
    'Relaxed through the leg with an easy rise and a washed finish. Built for slow mornings and long afternoons.',
    98.00, 'MAD', 'PUBLISHED', 'bottoms', FALSE,
    'https://images.unsplash.com/photo-1582418702059-97ebafb35d09?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
),
(
    'a0000000-0000-0000-0000-000000000402', 'FF-402', 'The City Bag',
    'A compact bag with a structured shape and enough room for a phone, wallet, and keys.',
    112.00, 'MAD', 'PUBLISHED', 'accessories', FALSE,
    'https://images.unsplash.com/photo-1584917865442-de89df76afd3?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
),
(
    'a0000000-0000-0000-0000-000000000103', 'FF-103', 'The Ribbed Top',
    'A close-fitting top in a soft stretch fabric. A comfortable first layer with a clean neckline.',
    44.00, 'MAD', 'PUBLISHED', 'tops', FALSE,
    'https://images.unsplash.com/photo-1503342217505-b0a15ec3261c?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
),
(
    'a0000000-0000-0000-0000-000000000203', 'FF-203', 'The Transit Coat',
    'A longline layer with a generous silhouette and practical pockets. Cut for easy movement over winter layers.',
    158.00, 'MAD', 'PUBLISHED', 'layers', FALSE,
    'https://images.unsplash.com/photo-1539533018447-63fcce2678e3?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
),
(
    'a0000000-0000-0000-0000-000000000303', 'FF-303', 'The Relaxed Trouser',
    'A gently tapered trouser with an easy fit and a clean front. Designed to work with both sneakers and loafers.',
    86.00, 'MAD', 'PUBLISHED', 'bottoms', FALSE,
    'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
),
(
    'a0000000-0000-0000-0000-000000000403', 'FF-403', 'The Weekend Pack',
    'A practical everyday pack with adjustable straps and space for a light layer and your daily essentials.',
    76.00, 'MAD', 'PUBLISHED', 'accessories', FALSE,
    'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=900&q=85', NOW(), NOW()
)
ON CONFLICT (sku) DO NOTHING;

-- 2. Insert Matching Initial Stock into Inventory
INSERT INTO inventory (id, product_id, available_quantity, reserved_quantity, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000101', 18, 0, NOW(), NOW()),
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000201', 3, 0, NOW(), NOW()),
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000301', 12, 0, NOW(), NOW()),
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000401', 14, 0, NOW(), NOW()),
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000102', 8, 0, NOW(), NOW()),
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000202', 7, 0, NOW(), NOW()),
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000302', 6, 0, NOW(), NOW()),
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000402', 0, 0, NOW(), NOW()), -- Sold out
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000103', 10, 0, NOW(), NOW()),
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000203', 2, 0, NOW(), NOW()),
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000303', 10, 0, NOW(), NOW()),
    (gen_random_uuid(), 'a0000000-0000-0000-0000-000000000403', 9, 0, NOW(), NOW())
ON CONFLICT (product_id) DO NOTHING;