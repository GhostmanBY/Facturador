package com.facturador.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.facturador.controller.AuthController;

public class LoginView {
    private AuthController authController;

    private VBox root;
    
    public LoginView(Stage stage) {
        root = new VBox(20);
        this.authController = new AuthController();
        
        TextField usuario = new TextField();
        PasswordField password = new PasswordField();

        Button ingresar = new Button("Ingresar");

        ingresar.setOnAction(e -> {
            boolean loginOk = this.authController.Login(
                usuario.getText(),
                password.getText()
            );

            if (loginOk) {
                System.out.print("Se ingreso correctamente");
                System.out.print(this.authController.getUserActual());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Contraseña y/o usuario incorrecto");
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(
            usuario,
            password,
            ingresar
        );
        root.setAlignment(Pos.CENTER);
    }

    public Parent getView() {
        return root;
    }
}