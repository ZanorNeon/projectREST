INSERT INTO users (email, password, fullname)
VALUES ('admin@example.com',
        '$2a$10$Dow1pQUnOtYt1wJv4FR8ZOe/UPuM9N4fYQZPfrxJ0cUO4nWx8mPLa',
        'Admin User');

INSERT INTO user_roles (user_id, role_id)
VALUES (
    (SELECT id FROM users WHERE email = 'admin@example.com'),
    (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
);