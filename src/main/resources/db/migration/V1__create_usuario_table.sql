CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    cpf VARCHAR(20) UNIQUE NOT NULL,
    zip_code VARCHAR(20),
    address VARCHAR(255),
    number INTEGER,
    complement VARCHAR(255),
    created_at DATE,
    status VARCHAR(20)
);
