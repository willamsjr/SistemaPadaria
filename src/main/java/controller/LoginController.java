package controller;

import app.MainApp; // Importa a classe principal
import dao.FuncionarioDAO;
import model.Funcionario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginController {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtSenha;

    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();


    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin() {
        String login = txtLogin.getText();
        String senha = txtSenha.getText();

        if (login.isEmpty() || senha.isEmpty()) {
            showAlert("Erro de Login", "Por favor, preencha os campos Email e Senha.", AlertType.WARNING);
            return;
        }

        Funcionario funcionarioLogado = funcionarioDAO.autenticar(login, senha);

        if (funcionarioLogado != null) {
            // SUCESSO NO LOGIN

            // Chama o método no MainApp para trocar a tela
            mainApp.showDashboardScreen();

        } else {
            // FALHA NO LOGIN
            showAlert("Erro de Login", "Email ou senha inválidos. Tente novamente.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}