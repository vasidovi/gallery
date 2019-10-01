drop table if exists oauth_client_details;
CREATE TABLE oauth_client_details (
                                                    client_id VARCHAR(256) PRIMARY KEY,
                                                    resource_ids VARCHAR(256),
                                                    client_secret VARCHAR(256) NOT NULL,
                                                    scope VARCHAR(256),
                                                    authorized_grant_types VARCHAR(256),
                                                    web_server_redirect_uri VARCHAR(256),
                                                    authorities VARCHAR(256),
                                                    access_token_validity INTEGER,
                                                    refresh_token_validity INTEGER,
                                                    additional_information VARCHAR(4000),
                                                    autoapprove VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS oauth_client_token (
                                                  token_id VARCHAR(256),
                                                  token BLOB,
                                                  authentication_id VARCHAR(256) PRIMARY KEY,
                                                  user_name VARCHAR(256),
                                                  client_id VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS oauth_access_token (
                                                  token_id VARCHAR(256),
                                                  token BLOB,
                                                  authentication_id VARCHAR(256),
                                                  user_name VARCHAR(256),
                                                  client_id VARCHAR(256),
                                                  authentication BLOB,
                                                  refresh_token VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
                                                   token_id VARCHAR(256),
                                                   token BLOB,
                                                   authentication BLOB
);

CREATE TABLE IF NOT EXISTS oauth_code (
                                          code VARCHAR(256), authentication BLOB
);

CREATE TABLE IF NOT EXISTS role
(id BIGINT NOT NULL AUTO_INCREMENT, role VARCHAR(256), PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS catalog
(id BIGINT NOT NULL AUTO_INCREMENT, name VARCHAR(256), PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS user
(id BIGINT NOT NULL AUTO_INCREMENT, username VARCHAR(256), password VARCHAR(256),  PRIMARY KEY (id));

drop table if exists user_role;
CREATE TABLE user_role (
                           user_id bigint NOT NULL,
                           role_id bigint NOT NULL,
                           CONSTRAINT FK859n2jvi8ivhui0rl0esws6o FOREIGN KEY (user_id) REFERENCES user (id),
                           CONSTRAINT FKa68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES role (id)
);


