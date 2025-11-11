package model;

import java.math.BigDecimal;

public class Item_venda {

    private Integer id;
    private Integer idVenda;
    private Produto produto;
    private int quantidade;
    private BigDecimal precoUnitario;

    public Item_venda() {
    }

    public Item_venda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Integer idVenda) {
        this.idVenda = idVenda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Integer getIdProduto() {
        return (produto != null) ? produto.getId() : null;
    }

    public String getProdutoNome() {
        return (produto != null) ? produto.getNome() : "Produto não encontrado";
    }

    public BigDecimal getPrecoTotalItem() {
        if (precoUnitario == null) {
            return BigDecimal.ZERO;
        }
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }
}