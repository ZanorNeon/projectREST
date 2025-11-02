INSERT INTO users (email, password, full_name)
VALUES ('admin@example.com',
        '$2a$10$/6smY4RZ0orZiTM3iZFPOeoOdzkVqMbFNnS8a/6cU.OiUCckCCy9W',
        'Admin User');

INSERT INTO user_roles (user_id, role_id)
VALUES (
    (SELECT id FROM users WHERE email = 'admin@example.com'),
    (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
);