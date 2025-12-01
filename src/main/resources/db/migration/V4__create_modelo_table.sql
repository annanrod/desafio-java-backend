CREATE TABLE model (
    id SERIAL PRIMARY KEY,
    fipe_model_id INTEGER NOT NULL,
    model_name VARCHAR(255) NOT NULL,
    brand_id INTEGER NOT NULL,
    CONSTRAINT fk_model_brand FOREIGN KEY (brand_id) REFERENCES brand(id)
);
