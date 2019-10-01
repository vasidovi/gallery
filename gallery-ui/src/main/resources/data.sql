
INSERT INTO role (role)
SELECT * FROM (SELECT 'USER') AS tmp
WHERE NOT EXISTS (
        SELECT role FROM role WHERE role = 'USER'
    ) LIMIT 1;

INSERT INTO role (role)
SELECT * FROM (SELECT 'ADMIN') AS tmp
WHERE NOT EXISTS (
        SELECT role FROM role WHERE role = 'ADMIN'
    ) LIMIT 1;

INSERT INTO catalog (name)
SELECT * FROM (SELECT 'Fantasy') AS tmp
WHERE NOT EXISTS (
        SELECT name FROM catalog WHERE name = 'Fantasy'
    ) LIMIT 1;

INSERT INTO catalog (name)
SELECT * FROM (SELECT 'Fiction') AS tmp
WHERE NOT EXISTS (
        SELECT name FROM catalog WHERE name = 'Fiction'
    ) LIMIT 1;

INSERT INTO catalog (name)
SELECT * FROM (SELECT 'Art') AS tmp
WHERE NOT EXISTS (
        SELECT name FROM catalog WHERE name = 'Art'
    ) LIMIT 1;

INSERT INTO catalog (name)
SELECT * FROM (SELECT 'Nature') AS tmp
WHERE NOT EXISTS (
        SELECT name FROM catalog WHERE name = 'Nature'
    ) LIMIT 1;

INSERT INTO catalog (name)
SELECT * FROM (SELECT 'Design') AS tmp
WHERE NOT EXISTS (
        SELECT name FROM catalog WHERE name = 'Design'
    ) LIMIT 1;

-- insert client details
INSERT INTO oauth_client_details
(client_id, client_secret, scope, authorized_grant_types,
 authorities, access_token_validity, refresh_token_validity)
VALUES
('testjwtclientid', '$2a$10$qtH0F1m488673KwgAfFXEOQvzT4DZJTf5dVfK0bG5ab9dwD1Ve.MW',
 'read,write', 'password,refresh_token,client_credentials,authorization_code',
 'ROLE_CLIENT,ROLE_TRUSTED_CLIENT', 900, 2592000);
