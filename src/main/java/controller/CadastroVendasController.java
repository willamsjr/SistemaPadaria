package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Item_venda;

public class CadastroVendasController {

    @FXML
    private TextField txtClienteCPF;
    @FXML
    private Button btnBuscarCliente;
    @FXML
    private Label lblNomeCliente;

    @FXML
    private TextField txtProdutoBusca;
    @FXML
    private TextField txtProdutoQtd;
    @FXML
    private Button btnAdicionarItem;

    @FXML
    private TableView<Item_venda> tblItensVenda;
    @FXML
    private TableColumn<Item_venda, String> colProdutoNome;
    @FXML
    private TableColumn<Item_venda, Integer> colProdutoQtd;
    @FXML
    private TableColumn<Item_venda, Double> colProdutoPrecoUnit;
    @FXML
    private TableColumn<Item_venda, Double> colProdutoPrecoTotal;
    @FXML
    private TableColumn<Item_venda, Void> colAcoes;

    @FXML
    private Label lblTotalVenda;
    @FXML
    private Button btnFinalizarVenda;

    @FXML
    private void initialize() {
        btnBuscarCliente.setOnAction(e -> handleBuscarCliente());
        btnAdicionarItem.setOnAction(e -> handleAdicionarItem());
        btnFinalizarVenda.setOnAction(e -> handleFinalizarVenda());
    }

    private void handleBuscarCliente() {
        System.out.println("Buscando cliente: " + txtClienteCPF.getText());
    }

    private void handleAdicionarItem() {
        System.out.println("Adicionando item: " + txtProdutoBusca.getText());
    }

    private void handleFinalizarVenda() {
        System.out.println("Finalizando venda...");
    }
}