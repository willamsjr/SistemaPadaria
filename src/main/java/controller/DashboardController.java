package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class DashboardController {

    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnCadastroVendas;
    @FXML
    private Button btnEstoque;
    @FXML
    private Button btnRelatorios;
    @FXML
    private Button btnAgendamento;
    @FXML
    private Button btnSair;

    @FXML
    private AnchorPane mainContentPane;

    @FXML
    public void initialize() {
        btnSair.setOnAction(e -> handleSair());
        btnCadastroVendas.setOnAction(e -> loadView("/view/CadastroVendas.fxml"));
    }

    private void handleSair() {
        Platform.exit();
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane newView = loader.load();

            mainContentPane.getChildren().clear();
            mainContentPane.getChildren().add(newView);

            AnchorPane.setTopAnchor(newView, 0.0);
            AnchorPane.setBottomAnchor(newView, 0.0);
            AnchorPane.setLeftAnchor(newView, 0.0);
            AnchorPane.setRightAnchor(newView, 0.0);

        } catch (IOException e) {
            System.err.println("Erro ao carregar a view: " + fxmlPath);
            e.printStackTrace();
        }
    }
}