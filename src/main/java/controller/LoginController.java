package controller;

import app.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblMensagemErro;

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin() {
        String usuario = txtUsuario.getText();
        String senha = txtSenha.getText();

        if ("admin".equals(usuario) && "123".equals(senha)) {
            lblMensagemErro.setText("");
            if (mainApp != null) {
                mainApp.showDashboardScreen();
            }
        } else {
            lblMensagemErro.setText("Usuário ou senha inválidos.");
        }
    }
}