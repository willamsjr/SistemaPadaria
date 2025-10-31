package model;

import java.math.BigDecimal;

public class Item_venda {
    private Integer id;
    private Integer idProduto;
    private int quantidade;
    private BigDecimal precoUnit;

    public Item_venda() {}

    public Item_venda(Integer id, Integer idProduto, int quantidade, BigDecimal precoUnit) {
        this.id = id;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.precoUnit = precoUnit;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdProduto() { return idProduto; }
    public void setIdProduto(Integer idProduto) { this.idProduto = idProduto; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnit() { return precoUnit; }
    public void setPrecoUnit(BigDecimal precoUnit) { this.precoUnit = precoUnit; }
}
