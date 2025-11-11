package controller;

import dao.VendaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Venda;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class RelatoriosController {

    private VendaDAO vendaDAO = new VendaDAO();

    private ObservableList<Venda> listaVendas = FXCollections.observableArrayList();
    private final NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML private DatePicker datePickerInicio;
    @FXML private DatePicker datePickerFim;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimpar;

    @FXML private TableView<Venda> tblVendas;
    @FXML private TableColumn<Venda, Integer> colIdVenda;
    @FXML private TableColumn<Venda, LocalDateTime> colData;
    @FXML private TableColumn<Venda, String> colCliente;
    @FXML private TableColumn<Venda, String> colFuncionario;
    @FXML private TableColumn<Venda, BigDecimal> colValorTotal;

    @FXML private Label lblTotalVendasPeriodo;
    @FXML private Label lblValorTotalPeriodo;

    @FXML
    private void initialize() {
        configurarTabela();
        datePickerFim.setValue(LocalDate.now());
        btnBuscar.setOnAction(e -> handleBuscarPeriodo());
        btnLimpar.setOnAction(e -> carregarTodasVendas());
        carregarTodasVendas();
    }

    private void configurarTabela() {
        tblVendas.setItems(listaVendas);
        colIdVenda.setCellValueFactory(new PropertyValueFactory<>("id"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colFuncionario.setCellValueFactory(new PropertyValueFactory<>("nomeFuncionario"));
    }

    private void carregarTodasVendas() {
        listaVendas.clear();
        List<Venda> vendasDoBanco = vendaDAO.buscarTodasVendas();
        listaVendas.addAll(vendasDoBanco);
        atualizarResumo();
    }

    private void handleBuscarPeriodo() {
        LocalDate inicioLD = datePickerInicio.getValue();
        LocalDate fimLD = datePickerFim.getValue();

        if (inicioLD == null || fimLD == null) {
            return;
        }

        LocalDateTime inicio = inicioLD.atStartOfDay();
        LocalDateTime fim = fimLD.atTime(23, 59, 59);

        List<Venda> vendasDoPeriodo = vendaDAO.buscarVendasPorPeriodo(inicio, fim);

        listaVendas.clear();
        listaVendas.addAll(vendasDoPeriodo);
        atualizarResumo();
    }

    private void atualizarResumo() {
        BigDecimal totalValor = BigDecimal.ZERO;
        int totalVendas = listaVendas.size();

        for (Venda venda : listaVendas) {
            totalValor = totalValor.add(venda.getValorTotal());
        }

        lblTotalVendasPeriodo.setText("Total de Vendas no Período: " + totalVendas);
        lblValorTotalPeriodo.setText("Valor Total no Período: " + formatadorMoeda.format(totalValor));
    }
}