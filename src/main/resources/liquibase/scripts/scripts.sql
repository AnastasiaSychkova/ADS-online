-- liquibase formatted sql

--changeset asychkova: 1
CREATE TABLE images
(
    id               BIGINT PRIMARY KEY,
    name             VARCHAR,
    contentType      VARCHAR,
    bytes            BYTEA
);

CREATE TABLE users
(
    id            BIGINT PRIMARY KEY,
    email         VARCHAR(255) UNIQUE NOT NULL,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    password      VARCHAR(255),
    phone         VARCHAR(255),
    register_date DATE,
    role          VARCHAR(255),
    image_id         BIGINT,
    FOREIGN KEY (image_id) REFERENCES images (id)
        ON DELETE CASCADE
);

CREATE TABLE ads
(
    id          BIGINT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    image_id    BIGINT,
    price       INT,
    title       VARCHAR(255),
    description varchar(255),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (image_id) REFERENCES images (id)
        ON DELETE CASCADE
);


CREATE TABLE comments
(
    id         BIGINT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    created_at BIGINT,
    text       TEXT,
    ad_id      BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (ad_id) REFERENCES ads (id)
        ON DELETE CASCADE
);
