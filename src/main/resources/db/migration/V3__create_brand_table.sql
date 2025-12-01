CREATE TABLE brand (
    id SERIAL PRIMARY KEY,
    fipe_brand_id INTEGER UNIQUE NOT NULL,
    brand_name VARCHAR(255) NOT NULL
);
