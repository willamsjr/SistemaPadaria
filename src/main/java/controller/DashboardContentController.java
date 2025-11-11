package controller;

import dao.AgendamentoDAO;
import dao.ProdutoDAO;
import dao.VendaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.NumberFormat;
import java.util.Locale;

public class DashboardContentController {

    private VendaDAO vendaDAO = new VendaDAO();
    private AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    @FXML private Label lblTotalVendasHoje;
    @FXML private Label lblAgendamentosPendentes;
    @FXML private Label lblEstoqueBaixo;


    @FXML
    public void initialize() {
        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
        // 1. Vendas de Hoje
        double totalVendas = vendaDAO.calcularTotalVendasHoje();
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        lblTotalVendasHoje.setText(nf.format(totalVendas));

        // 2. Agendamentos Pendentes
        int pendentes = agendamentoDAO.contarAgendamentosPendentes();
        lblAgendamentosPendentes.setText(String.valueOf(pendentes));

        // 3. Estoque Baixo
        int estoqueBaixo = produtoDAO.contarEstoqueBaixo();
        lblEstoqueBaixo.setText(String.valueOf(estoqueBaixo));
    }
}