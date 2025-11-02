INSERT INTO categories (name, slug, description)
VALUES
('Electronics', 'electronics', 'Electronic devices and gadgets'),
('Books', 'books', 'Books across all genres'),
('Clothing', 'clothing', 'All clothing');

INSERT INTO products (name, slug, description, price, currency, stock, active, category_id)
VALUES
('Smartphone', 'smartphone', 'Latest Android smartphone', 699.99, 'USD', 50, true,
 (SELECT id FROM categories WHERE slug = 'electronics')),
('Laptop', 'laptop', 'Powerful gaming laptop', 1299.99, 'USD', 20, true,
 (SELECT id FROM categories WHERE slug = 'electronics')),
('Novel', 'novel', 'Bestselling fiction book', 19.99, 'USD', 100, true,
 (SELECT id FROM categories WHERE slug = 'books'));