CREATE TABLE STOCK
(
    ID              INT PRIMARY KEY AUTO_INCREMENT,
    SKU_PRODUCT     VARCHAR(20) UNIQUE NOT NULL,                                        -- Código interno ou SKU do produto
    NAME_PRODUCT    VARCHAR(100)       NOT NULL,                                        -- Nome/descrição do produto
    QUANTITY_ACTUAL DECIMAL(10, 2)     NOT NULL DEFAULT 0,                              -- Quantidade atual em estoque
    TOTAL_VALUE     DECIMAL(10, 2),                                                     -- Estoque máximo recomendado
    VALUE_UNIT      DECIMAL(10, 2),                                                     -- Custo unitário
    VALUE_SALE      DECIMAL(10, 2),                                                     -- Preço de venda
    STATUS          ENUM('AVALIABLE','UNAVALIABLE','DISCONTINUED') DEFAULT 'AVALIABLE', -- Status do produto
    DT_REGISTER     TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP,              -- Data de criação do registro
);