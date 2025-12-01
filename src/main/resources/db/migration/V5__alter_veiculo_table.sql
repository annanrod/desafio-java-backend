ALTER TABLE veiculo ADD COLUMN brand_id INTEGER;
ALTER TABLE veiculo ADD COLUMN model_id INTEGER;
ALTER TABLE veiculo ADD COLUMN year_model INTEGER;
ALTER TABLE veiculo ADD COLUMN fipe_price NUMERIC(10,2);
ALTER TABLE veiculo ADD COLUMN fipe_code VARCHAR(50);

ALTER TABLE veiculo
    ADD CONSTRAINT fk_veiculo_brand FOREIGN KEY (brand_id) REFERENCES brand(id);

ALTER TABLE veiculo
    ADD CONSTRAINT fk_veiculo_model FOREIGN KEY (model_id) REFERENCES model(id);
