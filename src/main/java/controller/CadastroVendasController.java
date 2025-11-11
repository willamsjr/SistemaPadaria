package controller;

import dao.ClienteDAO;
import dao.ProdutoDAO;
import dao.VendaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import model.Cliente;
import model.Item_venda;
import model.Produto;
import model.Venda;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class CadastroVendasController {

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private VendaDAO vendaDAO = new VendaDAO();

    private ObservableList<Item_venda> carrinhoItens = FXCollections.observableArrayList();
    private final NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private Cliente clienteSelecionado = null;
    private final int ID_FUNCIONARIO_LOGADO = 1;

    @FXML private TextField txtClienteCPF;
    @FXML private Button btnBuscarCliente;
    @FXML private Label lblNomeCliente;
    @FXML private TextField txtProdutoBusca;
    @FXML private TextField txtProdutoQtd;
    @FXML private Button btnAdicionarItem;
    @FXML private TableView<Item_venda> tblItensVenda;
    @FXML private TableColumn<Item_venda, String> colProdutoNome;
    @FXML private TableColumn<Item_venda, Integer> colProdutoQtd;
    @FXML private TableColumn<Item_venda, BigDecimal> colProdutoPrecoUnit;
    @FXML private TableColumn<Item_venda, BigDecimal> colProdutoPrecoTotal;
    @FXML private TableColumn<Item_venda, Void> colAcoes;
    @FXML private Label lblTotalVenda;
    @FXML private Button btnFinalizarVenda;


    @FXML
    private void initialize() {
        tblItensVenda.setItems(carrinhoItens);

        colProdutoNome.setCellValueFactory(new PropertyValueFactory<>("produtoNome"));
        colProdutoQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colProdutoPrecoUnit.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        colProdutoPrecoTotal.setCellValueFactory(new PropertyValueFactory<>("precoTotalItem"));

        btnBuscarCliente.setOnAction(e -> handleBuscarCliente());
        btnAdicionarItem.setOnAction(e -> handleAdicionarItem());
        btnFinalizarVenda.setOnAction(e -> handleFinalizarVenda());

        atualizarTotalVenda();
        limparCliente();
    }

    private void handleBuscarCliente() {
        String cpf = txtClienteCPF.getText();
        if (cpf.isEmpty()) {
            limparCliente();
            return;
        }

        clienteSelecionado = clienteDAO.buscarPorCpf(cpf);

        if (clienteSelecionado != null) {
            lblNomeCliente.setText(clienteSelecionado.getNome());
            lblNomeCliente.setTextFill(Color.GREEN);
        } else {
            lblNomeCliente.setText("Cliente não encontrado.");
            lblNomeCliente.setTextFill(Color.RED);
        }
    }

    private void limparCliente() {
        clienteSelecionado = null;
        txtClienteCPF.clear();
        lblNomeCliente.setText("Nenhum cliente selecionado (Venda anônima)");
        lblNomeCliente.setTextFill(Color.GRAY);
    }

    private void handleAdicionarItem() {
        int produtoId;
        int quantidade;

        try {
            produtoId = Integer.parseInt(txtProdutoBusca.getText());
            quantidade = Integer.parseInt(txtProdutoQtd.getText());
            if (quantidade <= 0) {
                showAlert(Alert.AlertType.WARNING, "Erro de Quantidade", "A quantidade deve ser maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Erro de Entrada", "ID do Produto e Quantidade devem ser números.");
            return;
        }

        Produto produto = produtoDAO.buscarPorId(produtoId);
        if (produto == null) {
            showAlert(Alert.AlertType.ERROR, "Erro de Produto", "Produto com ID " + produtoId + " não encontrado.");
            return;
        }

        if (produto.getQntEstoque() < quantidade) {
            showAlert(Alert.AlertType.WARNING, "Erro de Estoque",
                    "Estoque insuficiente. Disponível: " + produto.getQntEstoque());
            return;
        }

        Item_venda novoItem = new Item_venda(produto, quantidade);
        carrinhoItens.add(novoItem);

        txtProdutoBusca.clear();
        txtProdutoQtd.setText("1");
        atualizarTotalVenda();
    }

    private BigDecimal calcularTotalVenda() {
        BigDecimal total = BigDecimal.ZERO;
        for (Item_venda item : carrinhoItens) {
            total = total.add(item.getPrecoTotalItem());
        }
        return total;
    }

    private void atualizarTotalVenda() {
        BigDecimal total = calcularTotalVenda();
        lblTotalVenda.setText("TOTAL: " + formatadorMoeda.format(total));
    }

    private void handleFinalizarVenda() {
        if (carrinhoItens.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Venda Vazia", "Adicione pelo menos um item ao carrinho.");
            return;
        }

        Venda novaVenda = new Venda();
        novaVenda.setIdFuncionario(ID_FUNCIONARIO_LOGADO);

        if (clienteSelecionado != null) {
            novaVenda.setIdCliente(clienteSelecionado.getId());
        }

        novaVenda.setData(LocalDateTime.now());
        novaVenda.setValorTotal(calcularTotalVenda());
        novaVenda.setItens(new ArrayList<>(carrinhoItens));

        boolean sucesso = vendaDAO.registrarVenda(novaVenda);

        if (sucesso) {
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Venda registrada com sucesso! ID: " + novaVenda.getId());
            limparFormularioVenda();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro na Venda", "A transação falhou. Verifique o estoque (RN02).");
        }
    }

    private void limparFormularioVenda() {
        carrinhoItens.clear();
        limparCliente();
        txtProdutoBusca.clear();
        txtProdutoQtd.setText("1");
        atualizarTotalVenda();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}