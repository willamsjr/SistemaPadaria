package controller;

import dao.AgendamentoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Agendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.ButtonType;

public class AgendamentoController {

    private AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    private ObservableList<Agendamento> listaAgendamentos = FXCollections.observableArrayList();

    @FXML private TableView<Agendamento> tblAgendamentos;
    @FXML private TableColumn<Agendamento, LocalDateTime> colData;
    @FXML private TableColumn<Agendamento, String> colCliente;
    @FXML private TableColumn<Agendamento, String> colDescricao;
    @FXML private TableColumn<Agendamento, String> colStatus;

    @FXML private DatePicker datePickerData;
    @FXML private TextField txtHora;
    @FXML private TextField txtClienteNome;
    @FXML private TextField txtClienteTelefone;
    @FXML private TextArea txtDescricao;
    @FXML private Button btnAgendar;
    @FXML private Button btnConcluirPedido;
    @FXML private Button btnDeletarPedido;

    @FXML
    private void initialize() {
        configurarTabela();
        carregarAgendamentos();
        btnAgendar.setOnAction(e -> handleAgendar());
        btnConcluirPedido.setOnAction(e -> handleConcluirPedido());
        btnDeletarPedido.setOnAction(e -> handleDeletarPedido());
        datePickerData.setValue(LocalDate.now());
    }

    private void configurarTabela() {
        tblAgendamentos.setItems(listaAgendamentos);
        colData.setCellValueFactory(new PropertyValueFactory<>("dataAgendamento"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricaoPedido"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void carregarAgendamentos() {
        listaAgendamentos.clear();
        List<Agendamento> agendamentosDoBanco = agendamentoDAO.buscarTodos();
        listaAgendamentos.addAll(agendamentosDoBanco);
    }

    private void handleAgendar() {
        LocalDate data = datePickerData.getValue();
        LocalTime hora;
        String nomeCliente = txtClienteNome.getText();
        String descricao = txtDescricao.getText();

        if (data == null || nomeCliente.isEmpty() || descricao.isEmpty() || txtHora.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos Obrigatórios", "Data, Hora, Nome do Cliente e Descrição são obrigatórios.");
            return;
        }

        try {
            hora = LocalTime.parse(txtHora.getText(), DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.WARNING, "Formato Inválido", "A Hora deve estar no formato HH:mm (ex: 08:30 ou 14:00).");
            return;
        }

        LocalDateTime dataAgendamento = data.atTime(hora);

        Agendamento novoAgendamento = new Agendamento();
        novoAgendamento.setDataAgendamento(dataAgendamento);
        novoAgendamento.setNomeCliente(nomeCliente);
        novoAgendamento.setTelefoneCliente(txtClienteTelefone.getText());
        novoAgendamento.setDescricaoPedido(descricao);
        novoAgendamento.setStatus("Pendente");

        boolean sucesso = agendamentoDAO.cadastrar(novoAgendamento);

        if (sucesso) {
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Pedido agendado com sucesso!");
            limparCamposCadastro();
            carregarAgendamentos();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro no Cadastro", "Não foi possível salvar o agendamento.");
        }
    }

    private void handleConcluirPedido() {
        Agendamento selecionado = tblAgendamentos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            showAlert(Alert.AlertType.WARNING, "Nenhum Item", "Por favor, selecione um agendamento na tabela primeiro.");
            return;
        }

        if (!selecionado.getStatus().equals("Pendente")) {
            showAlert(Alert.AlertType.INFORMATION, "Status Inválido", "Este agendamento já está " + selecionado.getStatus().toLowerCase() + ".");
            return;
        }

        boolean sucesso = agendamentoDAO.atualizarStatus(selecionado.getId(), "Concluído");

        if (sucesso) {
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Agendamento (ID: " + selecionado.getId() + ") marcado como Concluído.");
            carregarAgendamentos();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível atualizar o status no banco de dados.");
        }
    }

    private void handleDeletarPedido() {
        Agendamento selecionado = tblAgendamentos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            showAlert(Alert.AlertType.WARNING, "Nenhum Item", "Por favor, selecione um agendamento na tabela primeiro.");
            return;
        }

        if (!selecionado.getStatus().equals("Concluído")) {
            showAlert(Alert.AlertType.WARNING, "Ação Inválida", "Apenas agendamentos com status 'Concluído' podem ser apagados.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Apagar Agendamento ID: " + selecionado.getId());
        confirmacao.setContentText("Tem certeza que deseja apagar permanentemente este agendamento?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean sucesso = agendamentoDAO.deletar(selecionado.getId());
            if (sucesso) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Agendamento apagado.");
                carregarAgendamentos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível apagar o agendamento do banco de dados.");
            }
        }
    }

    private void limparCamposCadastro() {
        datePickerData.setValue(LocalDate.now());
        txtHora.clear();
        txtClienteNome.clear();
        txtClienteTelefone.clear();
        txtDescricao.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}