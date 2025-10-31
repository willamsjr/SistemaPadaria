package model;

import java.math.BigDecimal;

public class Produto {
    private Integer id;
    private String nome;
    private BigDecimal preco;
    private int qntEstoque;

    public Produto() {}

    public Produto(Integer id, String nome, BigDecimal preco, int qntEstoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.qntEstoque = qntEstoque;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public int getQntEstoque() { return qntEstoque; }
    public void setQntEstoque(int qntEstoque) { this.qntEstoque = qntEstoque; }
}