INSERT INTO users(email,password) VALUES ('admin@example.com','{noop}hardpass'),
                                         ('user@example.com','{noop}hardpass');

INSERT INTO user_role(name,description) VALUES ('ADMIN','Administrator'),
                                               ('USER','Standard user');

INSERT INTO user_roles(user_id,role_id) VALUES (1,1),
                                               (2,2);