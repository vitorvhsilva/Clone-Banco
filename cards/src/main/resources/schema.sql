DROP TABLE IF EXISTS tb_catalogo;
DROP TABLE IF EXISTS tb_cartoes;

CREATE TABLE tb_catalogo (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    bandeira VARCHAR(255) NOT NULL,
    segmento VARCHAR(255) NOT NULL
);

CREATE TABLE tb_cartoes (
    id_cartao UUID PRIMARY KEY,
    id_usuario VARCHAR(255) NOT NULL,
    id_catalogo SERIAL NOT NULL,
    nome_cartao VARCHAR(255) NOT NULL,
    nome_usuario VARCHAR(255) NOT NULL,
    numero_cartao VARCHAR(16) NOT NULL,
    codigo_seguranca VARCHAR(3) NOT NULL,
    agencia VARCHAR(255) NOT NULL,
    conta VARCHAR(255) NOT NULL,
    limite DECIMAL(19, 2) NOT NULL,
    bandeira VARCHAR(255) NOT NULL,
    segmento VARCHAR(255) NOT NULL,
    tipo_cartao VARCHAR(255) NOT NULL
);