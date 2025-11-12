package model;

import java.math.BigDecimal;

public class Produto {

    private Integer id;
    private String nome;
    private BigDecimal preco;
    private Integer qntEstoque;

    public Produto() {
    }

    public Produto(Integer id, String nome, BigDecimal preco, Integer qntEstoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.qntEstoque = qntEstoque;
    }


    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Integer getQntEstoque() {
        return qntEstoque;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public void setQntEstoque(Integer qntEstoque) {
        this.qntEstoque = qntEstoque;
    }
}