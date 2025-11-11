package controller;

import dao.ProdutoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Produto;

import java.math.BigDecimal;
import java.util.List;

public class EstoqueController {

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    private final int LIMITE_ESTOQUE_BAIXO = 10;
    private final String ESTILO_ESTOQUE_BAIXO = "low-stock-row";

    @FXML private TableView<Produto> tblProdutos;
    @FXML private TableColumn<Produto, Integer> colProdutoId;
    @FXML private TableColumn<Produto, String> colProdutoNome;
    @FXML private TableColumn<Produto, BigDecimal> colProdutoPreco;
    @FXML private TableColumn<Produto, Integer> colProdutoQnt;

    @FXML private TextField txtNovoNome;
    @FXML private TextField txtNovoPreco;
    @FXML private TextField txtNovoQnt;
    @FXML private Button btnCadastrarProduto;

    @FXML
    private void initialize() {
        configurarTabela();
        carregarProdutosNaTabela();
        btnCadastrarProduto.setOnAction(e -> handleCadastrarProduto());
    }

    private void configurarTabela() {
        tblProdutos.setItems(listaProdutos);
        colProdutoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colProdutoPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colProdutoQnt.setCellValueFactory(new PropertyValueFactory<>("qntEstoque"));

        tblProdutos.setRowFactory(tv -> new TableRow<Produto>() {
            @Override
            protected void updateItem(Produto produto, boolean empty) {
                super.updateItem(produto, empty);

                getStyleClass().remove(ESTILO_ESTOQUE_BAIXO);

                if (produto == null || empty) {
                    // Linha vazia
                } else if (produto.getQntEstoque() <= LIMITE_ESTOQUE_BAIXO) {
                    getStyleClass().add(ESTILO_ESTOQUE_BAIXO);
                }
            }
        });
    }

    private void carregarProdutosNaTabela() {
        listaProdutos.clear();
        List<Produto> produtosDoBanco = produtoDAO.buscarTodos();
        listaProdutos.addAll(produtosDoBanco);
    }

    private void handleCadastrarProduto() {
        String nome = txtNovoNome.getText();
        BigDecimal preco;
        int qntEstoque;

        if (nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campo Vazio", "O nome do produto é obrigatório.");
            return;
        }

        try {
            preco = new BigDecimal(txtNovoPreco.getText().replace(",", "."));
            qntEstoque = Integer.parseInt(txtNovoQnt.getText());

            if (preco.compareTo(BigDecimal.ZERO) < 0 || qntEstoque < 0) {
                showAlert(Alert.AlertType.WARNING, "Valor Inválido", "Preço e Quantidade não podem ser negativos.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Erro de Formato", "Preço e Quantidade devem ser números válidos.");
            return;
        }

        Produto novoProduto = new Produto();
        novoProduto.setNome(nome);
        novoProduto.setPreco(preco);
        novoProduto.setQntEstoque(qntEstoque);

        boolean sucesso = produtoDAO.cadastrar(novoProduto);

        if (sucesso) {
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Produto cadastrado com sucesso!");
            limparCamposCadastro();
            carregarProdutosNaTabela();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro no Cadastro", "Não foi possível cadastrar o produto no banco.");
        }
    }

    private void limparCamposCadastro() {
        txtNovoNome.clear();
        txtNovoPreco.clear();
        txtNovoQnt.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}