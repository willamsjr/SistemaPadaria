DROP DATABASE IF EXISTS padaria;

-- 🔹 Cria o novo banco
CREATE DATABASE padaria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE padaria;

-- ============================================================
-- TABELA: FUNCIONARIO
-- ============================================================
CREATE TABLE Funcionario (
    id_funcionario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha_hash VARCHAR(100) NOT NULL
);

-- ============================================================
-- TABELA: CLIENTE
-- ============================================================
CREATE TABLE Cliente (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(15) UNIQUE NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    endereco VARCHAR(255)
);

-- ============================================================
-- TABELA: PRODUTO
-- ============================================================
CREATE TABLE Produto (
    id_produto INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) UNIQUE NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    qnt_estoque INT NOT NULL
);

-- ============================================================
-- TABELA: VENDA
-- ============================================================
CREATE TABLE Venda (
    id_venda INT PRIMARY KEY AUTO_INCREMENT,
    id_funcionario INT NOT NULL,
    id_cliente INT,
    data DATETIME NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    forma_pagamento VARCHAR(50),
    FOREIGN KEY (id_funcionario) REFERENCES Funcionario(id_funcionario),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

-- ============================================================
-- TABELA: ITEM_VENDA
-- ============================================================
CREATE TABLE Item_Venda (
    id_item INT PRIMARY KEY AUTO_INCREMENT,
    id_venda INT NOT NULL,
    id_produto INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unit DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NULL DEFAULT NULL,
    FOREIGN KEY (id_venda) REFERENCES Venda(id_venda) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto)
);

-- ============================================================
-- TABELA: AGENDAMENTO (CORRIGIDA)
-- ============================================================
CREATE TABLE Agendamento (
    id_agendamento INT PRIMARY KEY AUTO_INCREMENT,
    data_agendamento DATETIME NOT NULL,
    nome_cliente VARCHAR(100) NOT NULL,
    telefone_cliente VARCHAR(20),
    descricao_pedido VARCHAR(255),
    status VARCHAR(20) NOT NULL
);

-- ============================================================
-- TABELA: ITEM_AGENDAMENTO (ainda opcional no seu sistema)
-- ============================================================
CREATE TABLE Item_Agendamento (
    id_item_agendamento INT PRIMARY KEY AUTO_INCREMENT,
    id_agendamento INT NOT NULL,
    id_produto INT NOT NULL,
    quantidade INT NOT NULL,
    FOREIGN KEY (id_agendamento) REFERENCES Agendamento(id_agendamento) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto)
);

-- ============================================================
-- TRIGGERS DE ESTOQUE E SUBTOTAL
-- ============================================================

DROP TRIGGER IF EXISTS trg_calcula_subtotal;
DROP TRIGGER IF EXISTS trg_baixa_estoque_venda;
DROP TRIGGER IF EXISTS trg_retorna_estoque_venda;

DELIMITER $$
CREATE TRIGGER trg_calcula_subtotal
BEFORE INSERT ON Item_Venda
FOR EACH ROW
BEGIN
    IF NEW.subtotal IS NULL THEN
        SET NEW.subtotal = NEW.preco_unit * NEW.quantidade;
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER trg_baixa_estoque_venda
AFTER INSERT ON Item_Venda
FOR EACH ROW
BEGIN
    UPDATE Produto
    SET qnt_estoque = qnt_estoque - NEW.quantidade
    WHERE id_produto = NEW.id_produto;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER trg_retorna_estoque_venda
AFTER DELETE ON Item_Venda
FOR EACH ROW
BEGIN
    UPDATE Produto
    SET qnt_estoque = qnt_estoque + OLD.quantidade
    WHERE id_produto = OLD.id_produto;
END$$
DELIMITER ;

-- ============================================================
-- DADOS INICIAIS
-- ============================================================

INSERT INTO Funcionario (nome, login, senha_hash)
VALUES 
('Victor Hugo', 'victor', '1234'),
('João Henrique', 'joao', '4321');

INSERT INTO Cliente (nome, telefone, cpf, endereco)
VALUES 
('Maria Silva', '79998887766', '123.456.789-10', 'Rua das Flores, 100'),
('Carlos Santos', '79997776655', '987.654.321-00', 'Av. Central, 200');

INSERT INTO Produto (nome, preco, qnt_estoque)
VALUES 
('Pão Francês', 0.50, 200),
('Bolo de Chocolate', 25.00, 10),
('Coxinha', 5.00, 50),
('Sonho de Creme', 6.00, 30),
('Pão Frances Teste Venda', 0.60, 40);

SELECT 'Banco padaria criado e populado com sucesso — versão compatível com seu sistema!' AS Status;

ALTER TABLE Produto ADD COLUMN limite_minimo INT NOT NULL DEFAULT 5;

ALTER TABLE Produto 
ADD COLUMN estoque_minimo INT NOT NULL DEFAULT 5;

INSERT INTO funcionario (nome, login, senha_hash)
VALUES ('Administrador', 'admin', '123');

ALTER TABLE Funcionario
ADD COLUMN cargo VARCHAR(20) NOT NULL DEFAULT 'Funcionario';

UPDATE Funcionario
SET cargo = 'Administrador'
WHERE login = 'admin';
