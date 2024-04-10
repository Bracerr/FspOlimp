CREATE TABLE users
(
    id         BIGSERIAL NOT NULL,
    email      VARCHAR(255),
    enabled    BOOLEAN,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    password   VARCHAR(120),
    patronymic VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    user_id BIGINT  NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE confirmation_token
(
    token_id           BIGINT NOT NULL,
    confirmation_token VARCHAR(255),
    created_date       TIMESTAMP(6),
    expiry_date        TIMESTAMP(6),
    user_id            BIGINT NOT NULL,
    PRIMARY KEY (token_id)
);

CREATE TABLE password_recovery_token
(
    token_id                BIGINT NOT NULL,
    created_date            TIMESTAMP(6),
    expiry_date             TIMESTAMP(6),
    password_recovery_token VARCHAR(255),
    user_id                 BIGINT NOT NULL,
    PRIMARY KEY (token_id)
);

CREATE TABLE roles
(
    id   SERIAL NOT NULL,
    name VARCHAR(20) CHECK (name IN ('ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN')),
    PRIMARY KEY (id)
);



ALTER TABLE IF EXISTS confirmation_token
    DROP CONSTRAINT IF EXISTS UK_3rtt9efhavjo2dfx9f763sypm;
ALTER TABLE IF EXISTS confirmation_token
    ADD CONSTRAINT UK_3rtt9efhavjo2dfx9f763sypm UNIQUE (user_id);

ALTER TABLE IF EXISTS password_recovery_token
    DROP CONSTRAINT IF EXISTS UK_297d13rkyrp33fttiymw0gtcp;
ALTER TABLE IF EXISTS password_recovery_token
    ADD CONSTRAINT UK_297d13rkyrp33fttiymw0gtcp UNIQUE (user_id);

ALTER TABLE IF EXISTS users
    DROP CONSTRAINT IF EXISTS UK6dotkott2kjsp8vw4d0m25fb7;
ALTER TABLE IF EXISTS users
    ADD CONSTRAINT UK6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);

CREATE SEQUENCE confirmation_token_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE password_recovery_token_seq START WITH 1 INCREMENT BY 50;

ALTER TABLE IF EXISTS confirmation_token
    ADD CONSTRAINT FKah4p1rycwibwm6s9bsyeckq51 FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE IF EXISTS password_recovery_token
    ADD CONSTRAINT FKaibuvih1s7vsqeg3h79f8r5g2 FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE IF EXISTS user_roles
    ADD CONSTRAINT FKh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES roles (id);
ALTER TABLE IF EXISTS user_roles
    ADD CONSTRAINT FKhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES users (id);



INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');



INSERT INTO users (first_name, last_name, patronymic, email, password, enabled)
VALUES ('Andrey', 'Kirgizov', 'Gennadievich', 'niker299@gmail.com', '$2a$10$ViObryPi8J2UWZKrkCUzQ.SzvU5Vv1t2PL4TpxVHSIGoMHugaKYzC', true);

WITH inserted_user AS (
    SELECT id FROM users WHERE email = 'niker299@gmail.com'
)

INSERT INTO user_roles (user_id, role_id)
SELECT id, (SELECT id FROM roles WHERE name = 'ROLE_ADMIN') FROM inserted_user;


