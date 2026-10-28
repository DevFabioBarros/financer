CREATE DATABASE IF NOT EXISTS financer
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE financer;

CREATE TABLE IF NOT EXISTS lancamentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL,
    descricao VARCHAR(120) NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    data DATE NOT NULL,
    categoria VARCHAR(60) NOT NULL,
    comentario VARCHAR(255) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(60) NOT NULL UNIQUE
);

INSERT IGNORE INTO categorias (nome) VALUES
    ('Casa'),
    ('Lazer'),
    ('Investimento'),
    ('Alimentacao'),
    ('Transporte'),
    ('Mercado');
