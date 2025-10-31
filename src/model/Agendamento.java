package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Agendamento {
    private Integer id;
    private String nomeSolicitante;
    private String telefone;
    private LocalDate data;
    private LocalTime hora;
    private String descricao;
    private String status;

    public Agendamento() {}

    public Agendamento(Integer id, String nomeSolicitante, String telefone, LocalDate data, LocalTime hora, String descricao, String status) {
        this.id = id;
        this.nomeSolicitante = nomeSolicitante;
        this.telefone = telefone;
        this.data = data;
        this.hora = hora;
        this.descricao = descricao;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNomeSolicitante() { return nomeSolicitante; }
    public void setNomeSolicitante(String nomeSolicitante) { this.nomeSolicitante = nomeSolicitante; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

