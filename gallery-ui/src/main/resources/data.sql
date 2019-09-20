create table if not exists role
(id bigint not null auto_increment, role varchar(255), primary key (id));

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

create table if not exists catalog
(id bigint not null auto_increment, name varchar(255), primary key (id));

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
