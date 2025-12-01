CREATE TABLE veiculo (
    id SERIAL PRIMARY KEY,
    plate VARCHAR(20) UNIQUE NOT NULL,
    advertised_price NUMERIC(15,2),
    year INTEGER,
    created_at DATE,
    usuario_id INTEGER,
    CONSTRAINT fk_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);
