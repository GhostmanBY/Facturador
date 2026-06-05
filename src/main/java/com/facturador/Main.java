package com.facturador;

import com.facturador.view.LoginView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView(stage);

        Scene scene = new Scene(loginView.getView(), 400, 300);

        scene.getStylesheets().add(getClass().getResource("/assets/login.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/assets/main.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/assets/factura.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/assets/dialog.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/assets/facturas.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/assets/panelAdmin.css").toExternalForm());

        stage.setTitle("Facturador");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}