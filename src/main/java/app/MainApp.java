package app;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Delícias Do Trigo - Sistema");

        // Inicia com a tela de login
        showLoginScreen();
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/Login.fxml"));
            StackPane loginLayout = loader.load();

            // Ajuste de tamanho da cena para o layout do Login (com fundo)
            Scene scene = new Scene(loginLayout, 600, 500);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            // Dá ao controller acesso ao MainApp
            LoginController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a tela de Login.");
        }
    }

    public void showDashboardScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/Dashboard.fxml"));
            BorderPane dashboardLayout = loader.load();

            // Cria uma nova cena com o layout da Dashboard
            Scene scene = new Scene(dashboardLayout);

            // Troca a cena no Stage (janela) principal
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.centerOnScreen();


        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a tela da Dashboard.");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}