-- init.sql

-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS cafe_server;

-- 사용할 데이터베이스 선택
USE cafe_server;

CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `phone_number` VARCHAR(255) UNIQUE,
    `password` VARCHAR(255),
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS categories (
    categories_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS product (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    category VARCHAR(255),
    price INT,
    cost INT,
    name VARCHAR(255) UNIQUE,
    choseong_name VARCHAR(255) UNIQUE,
    description VARCHAR(255),
    barcode VARCHAR(255),
    expiration_date TIMESTAMP,
    size VARCHAR(50)
);

ALTER TABLE product
ADD CONSTRAINT fk_user
FOREIGN KEY (user_id) REFERENCES user(id)
ON DELETE RESTRICT;

ALTER TABLE product
ADD CONSTRAINT fk_categories FOREIGN KEY (category)
REFERENCES categories(name)
ON DELETE RESTRICT;