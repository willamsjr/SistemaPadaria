package app;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistema de Padaria - Login");

        showLoginScreen();
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/Login.fxml"));

            StackPane loginLayout = loader.load();

            Scene scene = new Scene(loginLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            LoginController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de Login.");
            e.printStackTrace();
        }
    }

    public void showDashboardScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/MenuPrincipal.fxml"));

            AnchorPane dashboardLayout = loader.load();

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Sistema de Padaria - Dashboard");
            dashboardStage.setScene(new Scene(dashboardLayout));
            dashboardStage.show();

            if (primaryStage != null) {
                primaryStage.close();
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela da Dashboard.");
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}