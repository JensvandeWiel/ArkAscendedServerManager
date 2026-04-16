CREATE TABLE clusters
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

ALTER TABLE servers ADD COLUMN cluster UUID REFERENCES clusters(id);