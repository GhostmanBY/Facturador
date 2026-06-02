package com.facturador;

import com.facturador.view.LoginView;
import com.facturador.view.MainView;
import com.facturador.model.User;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        User user = User.builder()
        .id(1)
        .name("Nahuel Romero")
        .email("nahuel@example.com")
        .role("Administrador")
        .build();

        MainView loginView = new MainView(stage, user);

        Scene scene = new Scene(loginView.getView(), 400, 300);

        scene.getStylesheets().add(getClass().getResource("/assets/login.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/assets/main.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/assets/factura.css").toExternalForm());

        stage.setTitle("Facturador");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}