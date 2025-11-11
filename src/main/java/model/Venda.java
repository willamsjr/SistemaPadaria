package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Venda {

    private Integer id;
    private LocalDateTime data;
    private BigDecimal valorTotal;
    private Integer idFuncionario;
    private Integer idCliente;

    private String nomeFuncionario;
    private String nomeCliente;

    private List<Item_venda> itens;

    public Venda() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public List<Item_venda> getItens() {
        return itens;
    }

    public void setItens(List<Item_venda> itens) {
        this.itens = itens;
    }


    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public String getNomeCliente() {
        if (nomeCliente == null || nomeCliente.isEmpty()) {
            return "Venda Anônima";
        }
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
}