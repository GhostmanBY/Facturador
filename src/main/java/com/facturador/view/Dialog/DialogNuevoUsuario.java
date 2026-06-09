package com.facturador.view.Dialog;

import java.util.Optional;

import com.facturador.model.User;
import com.facturador.model.User.UserRole;
import com.facturador.view.Helpers.ErrorAlert;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DialogNuevoUsuario {
    private ErrorAlert errorAlert = new ErrorAlert();

    private final User usuarioExistente;

    public DialogNuevoUsuario() {
        this.usuarioExistente = null;
    }

    public DialogNuevoUsuario(User usuarioExistente) {
        this.usuarioExistente = usuarioExistente;
    }

    public Optional<User> mostrarDialogo() {
        Dialog<User> dialogo = new Dialog<>();
        dialogo.setTitle(usuarioExistente != null ? "Editar Usuario" : "Nuevo Usuario");

        DialogPane panelDialogo = dialogo.getDialogPane();
        panelDialogo.getStylesheets().add(getClass().getResource("/assets/dialog.css").toExternalForm());
        panelDialogo.getStyleClass().add("dialog-pane");

        // ── Campos ────────────────────────────────────────────────────
        TextField campoNombre = crearCampoTexto("Ej: Juan González");
        TextField campoEmail = crearCampoTexto("Ej: juan@empresa.com");
        TextField campoDocumento = crearCampoTexto("Ej: 30456789");
        TextField campoDomicilio = crearCampoTexto("Ej: Av. Colón 1234");
        TextField campoTelefono = crearCampoTexto("Ej: 2234445566");

        PasswordField campoPassword = new PasswordField();
        campoPassword.setPromptText("Contraseña");
        campoPassword.getStyleClass().add("dialog-text-field");
        PasswordField campoConfirmarPassword = new PasswordField();
        campoConfirmarPassword.setPromptText("Repetir contraseña");
        campoConfirmarPassword.getStyleClass().add("dialog-text-field");
        
        ComboBox<UserRole> comboRol = new ComboBox<>();
        comboRol.getItems().addAll(UserRole.values());
        comboRol.setPromptText("Seleccionar rol");
        comboRol.getStyleClass().add("dialog-combo");
        comboRol.setMaxWidth(Double.MAX_VALUE);
        if (usuarioExistente != null) {
            campoNombre.setText(usuarioExistente.getName());
            campoEmail.setText(usuarioExistente.getEmail());
            campoDocumento.setText(usuarioExistente.getDocumento());
            campoDomicilio.setText(usuarioExistente.getDomicilio());
            campoTelefono.setText(usuarioExistente.getTelefono());
            comboRol.setValue(usuarioExistente.getRole());
        }

        // ── Layout en grid de 2 columnas ──────────────────────────────
        GridPane grilla = new GridPane();
        grilla.setHgap(12);
        grilla.setVgap(10);

        ColumnConstraints columnaIzquierda = new ColumnConstraints();
        columnaIzquierda.setPercentWidth(50);
        ColumnConstraints columnaDerecha = new ColumnConstraints();
        columnaDerecha.setPercentWidth(50);
        grilla.getColumnConstraints().addAll(columnaIzquierda, columnaDerecha);

        grilla.add(crearCampoConLabel("Nombre", campoNombre),0, 0);
        grilla.add(crearCampoConLabel("Email", campoEmail),1, 0);
        grilla.add(crearCampoConLabel("Documento", campoDocumento), 0, 1);
        grilla.add(crearCampoConLabel("Teléfono", campoTelefono),  1, 1);
        grilla.add(crearCampoConLabel("Domicilio", campoDomicilio), 0, 2, 2, 1);
        grilla.add(crearCampoConLabel("Contraseña", campoPassword),  0, 3);
        grilla.add(crearCampoConLabel("Confirmar contraseña", campoConfirmarPassword), 1, 3);
        grilla.add(crearCampoConLabel("Rol", comboRol), 0, 4, 2, 1);

        // ── Botones ───────────────────────────────────────────────────
        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");
        btnGuardar.getStyleClass().add("btn-primary");
        btnCancelar.getStyleClass().add("btn-ghost");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnCancelar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnGuardar, Priority.ALWAYS);
        HBox.setHgrow(btnCancelar, Priority.ALWAYS);

        HBox filaBotones = new HBox(8, btnCancelar, btnGuardar);
        filaBotones.setAlignment(Pos.CENTER_RIGHT);

        VBox contenido = new VBox(16, grilla, filaBotones);
        contenido.getStyleClass().add("dialog-header-custom");
        panelDialogo.setContent(contenido);

        // Sin ButtonType del pane — los manejamos manualmente
        panelDialogo.getButtonTypes().add(ButtonType.CANCEL);
        Button botonCancelarOculto = (Button) panelDialogo.lookupButton(ButtonType.CANCEL);
        botonCancelarOculto.setVisible(false);
        botonCancelarOculto.setManaged(false);

        // ── Resultado ─────────────────────────────────────────────────
        User[] resultado = {null};

        btnCancelar.setOnAction(evento -> dialogo.close());

        btnGuardar.setOnAction(evento -> {
            String nombre = campoNombre.getText().trim();
            String email = campoEmail.getText().trim();
            String documento = campoDocumento.getText().trim();
            String domicilio = campoDomicilio.getText().trim();
            String telefono = campoTelefono.getText().trim();
            String contrasena = campoPassword.getText();
            String contrasenaConfirm = campoConfirmarPassword.getText();
            UserRole rolSeleccionado = comboRol.getValue();

            if (nombre.isBlank()) {
                errorAlert.mostrarError("El nombre no puede estar vacío");
                return;
            }
            if (email.isBlank()) {
                errorAlert.mostrarError("El email no puede estar vacío");
                return;
            }
            if (documento.isBlank()) {
                errorAlert.mostrarError("El documento no puede estar vacío");
                return;
            }
            if (contrasena.isBlank() && this.usuarioExistente != null) {
                errorAlert.mostrarError("La contraseña no puede estar vacía");
                return;
            }
            if (!contrasena.equals(contrasenaConfirm) && this.usuarioExistente != null) {
                errorAlert.mostrarError("Las contraseñas no coinciden");
                return;
            }
            if (rolSeleccionado == null) {
                errorAlert.mostrarError("Seleccioná un rol");
                return;
            }

            if (contrasena.isBlank() && usuarioExistente == null) {
                errorAlert.mostrarError("La contraseña no puede estar vacía");
                return;
            }
            if (!contrasena.isBlank() && !contrasena.equals(contrasenaConfirm)) {
                errorAlert.mostrarError("Las contraseñas no coinciden");
                return;
            }

            resultado[0] = User.builder()
            .id(usuarioExistente != null ? usuarioExistente.getId() : null)
            .name(nombre)
            .email(email)
            .documento(documento)
            .domicilio(domicilio)
            .telefono(telefono)
            .hashPassword(contrasena.isBlank() && usuarioExistente != null ? usuarioExistente.getHashPassword() : contrasena)
            .role(rolSeleccionado)
            .isActive(usuarioExistente != null ? usuarioExistente.isActive() : true)
            .build();

            dialogo.close();
        });

        dialogo.showAndWait();
        return Optional.ofNullable(resultado[0]);
    }

    // ── Helpers ───────────────────────────────────────────────────────
    private TextField crearCampoTexto(String placeholder) {
        TextField campo = new TextField();
        campo.setPromptText(placeholder);
        campo.getStyleClass().add("dialog-text-field");
        return campo;
    }

    private VBox crearCampoConLabel(String etiqueta, javafx.scene.Node campo) {
        VBox contenedor = new VBox(5);
        Label labelEtiqueta = new Label(etiqueta);
        labelEtiqueta.getStyleClass().add("dialog-subtitle-label");
        GridPane.setHgrow(contenedor, Priority.ALWAYS);
        contenedor.getChildren().addAll(labelEtiqueta, campo);
        return contenedor;
    }
}