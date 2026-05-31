package com.facturador.view;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.facturador.controller.AuthController;

public class LoginView {

    private final AuthController authController;
    private final VBox root;

    public LoginView(Stage stage) {
        this.authController = new AuthController();

        Label title = new Label("Mega Facturador++");
        title.getStyleClass().add("label-title");

        VBox cardTitle = new VBox(6, title);
        cardTitle.getStyleClass().add("card-title");

        Label usuarioLabel = new Label("USUARIO");
        usuarioLabel.getStyleClass().add("field-label");

        TextField usuario = new TextField();
        usuario.getStyleClass().add("text-field");
        usuario.setPromptText("tu@email.com");

        Label passwordLabel = new Label("CONTRASEÑA");
        passwordLabel.getStyleClass().add("field-label");

        PasswordField password = new PasswordField();
        password.getStyleClass().add("password-field");
        password.setPromptText("••••••••");

        Button btnLogin = new Button("Iniciar sesión");
        btnLogin.getStyleClass().add("button");
        btnLogin.setMaxWidth(Double.MAX_VALUE);

        Label errorLabel = new Label("Usuario o contraseña incorrectos");
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        btnLogin.setOnAction(e -> {
            boolean loginOk = authController.Login(
                usuario.getText(),
                password.getText()
            );

            if (loginOk) {
                MainView mainView = new MainView(stage, this.authController.getUserActual());
                stage.getScene().setRoot(mainView.getView());
            } else {
                errorLabel.setVisible(true);
                errorLabel.setOpacity(0);

                FadeTransition ft = new FadeTransition(
                    Duration.millis(200),
                    errorLabel
                );
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            }
        });

        VBox cardForm = new VBox(
            14,
            usuarioLabel,
            usuario,
            passwordLabel,
            password,
            errorLabel,
            btnLogin
        );
        cardForm.getStyleClass().add("card-form");

        this.root = new VBox(16, cardTitle, cardForm);
        this.root.getStyleClass().add("root");
        this.root.setAlignment(Pos.CENTER);
    }

    public Parent getView() {
        return root;
    }
}