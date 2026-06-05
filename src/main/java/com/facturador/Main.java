package com.facturador;

import java.sql.SQLException;

import com.facturador.database.createDB;
import com.facturador.view.LoginView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private createDB createdb  = new createDB();

    @Override
    public void start(Stage stage) {
        if (!createdb.databaseExists()) {
            try {
                createdb.createDatabase();
                createdb.createTables();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }

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