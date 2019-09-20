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


