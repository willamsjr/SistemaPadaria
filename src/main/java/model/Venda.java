package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Venda {
    private Integer id;
    private Integer idCliente; // pode ser nulo se a venda for sem cadastro
    private Integer idFuncionario;
    private LocalDateTime data;
    private BigDecimal valorTotal;
    private List<Item_venda> itens; // <-- alterado aqui

    public Venda() {}

    public Venda(Integer id, Integer idCliente, Integer idFuncionario, LocalDateTime data, BigDecimal valorTotal, List<Item_venda> itens) {
        this.id = id;
        this.idCliente = idCliente;
        this.idFuncionario = idFuncionario;
        this.data = data;
        this.valorTotal = valorTotal;
        this.itens = itens;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public Integer getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(Integer idFuncionario) { this.idFuncionario = idFuncionario; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public List<Item_venda> getItens() { return itens; } // <-- também alterado
    public void setItens(List<Item_venda> itens) { this.itens = itens; } // idem
}
