ALTER TABLE veiculo ADD COLUMN year_fabrication INTEGER;
ALTER TABLE veiculo DROP COLUMN IF EXISTS advertised_price;
ALTER TABLE veiculo DROP COLUMN IF EXISTS year;
