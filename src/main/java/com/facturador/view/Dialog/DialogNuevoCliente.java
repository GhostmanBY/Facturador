package com.facturador.view.Dialog;

import java.util.Optional;

import com.facturador.model.Cliente;
import com.facturador.view.Helpers.ErrorAlert;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class DialogNuevoCliente {
    private ErrorAlert errorAlert = new ErrorAlert();

    private final Cliente clienteExistente;

    public DialogNuevoCliente() {
        this.clienteExistente = null;
    }

    public DialogNuevoCliente(Cliente clienteExistente) {
        this.clienteExistente = clienteExistente;
    }

    public Optional<Cliente> mostrarDialogo() {
        Dialog<Cliente> dialogo = new Dialog<>();
        dialogo.setTitle(clienteExistente != null ? "Editar Cliente" : "Nuevo Cliente");

        DialogPane panelDialogo = dialogo.getDialogPane();
        panelDialogo.getStylesheets().add(
            getClass().getResource("/assets/dialog.css").toExternalForm()
        );
        panelDialogo.getStyleClass().add("dialog-pane");

        TextField campoNombre = crearCampoTexto("Ej: Juan González");
        TextField campoEmail = crearCampoTexto("Ej: juan@gmail.com");
        TextField campoDocumento = crearCampoTexto("Ej: 30456789");
        TextField campoDireccion = crearCampoTexto("Ej: Av. Colón 1234");
        TextField campoTelefono = crearCampoTexto("Ej: 2234445566");

        if (clienteExistente != null) {
            campoNombre.setText(clienteExistente.getNombre());
            campoEmail.setText(clienteExistente.getEmail());
            campoDocumento.setText(clienteExistente.getDocumento());
            campoDireccion.setText(clienteExistente.getDireccion());
            campoTelefono.setText(clienteExistente.getTelefono());
        }

        GridPane grilla = new GridPane();
        grilla.setHgap(12);
        grilla.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        grilla.getColumnConstraints().addAll(col1, col2);

        grilla.add(crearCampoConLabel("Nombre", campoNombre), 0, 0);
        grilla.add(crearCampoConLabel("Email", campoEmail), 1, 0);
        grilla.add(crearCampoConLabel("Documento", campoDocumento), 0, 1);
        grilla.add(crearCampoConLabel("Teléfono", campoTelefono), 1, 1);
        grilla.add(crearCampoConLabel("Dirección", campoDireccion), 0, 2, 2, 1);

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.getStyleClass().add("btn-primary");
        btnCancelar.getStyleClass().add("btn-ghost");

        HBox filaBotones = new HBox(8, btnCancelar, btnGuardar);
        filaBotones.setAlignment(Pos.CENTER_RIGHT);

        VBox contenido = new VBox(16, grilla, filaBotones);
        panelDialogo.setContent(contenido);

        panelDialogo.getButtonTypes().add(ButtonType.CANCEL);

        Button oculto = (Button) panelDialogo.lookupButton(ButtonType.CANCEL);
        oculto.setVisible(false);
        oculto.setManaged(false);

        Cliente[] resultado = {null};

        btnCancelar.setOnAction(e -> dialogo.close());

        btnGuardar.setOnAction(e -> {

            String nombre = campoNombre.getText().trim();
            String email = campoEmail.getText().trim();
            String documento = campoDocumento.getText().trim();
            String direccion = campoDireccion.getText().trim();
            String telefono = campoTelefono.getText().trim();

            if (nombre.isBlank()) {
                errorAlert.mostrarError("El nombre no puede estar vacío");
                return;
            }

            if (documento.isBlank()) {
                errorAlert.mostrarError("El documento no puede estar vacío");
                return;
            }

            resultado[0] = Cliente.builder()
                .id(clienteExistente != null ? clienteExistente.getId() : 0)
                .nombre(nombre)
                .email(email)
                .documento(documento)
                .direccion(direccion)
                .telefono(telefono)
                .isActive(clienteExistente != null ? clienteExistente.isActive() : true)
                .build();

            dialogo.close();
        });

        dialogo.showAndWait();
        return Optional.ofNullable(resultado[0]);
    }

    private TextField crearCampoTexto(String placeholder) {
        TextField campo = new TextField();
        campo.setPromptText(placeholder);
        campo.getStyleClass().add("dialog-text-field");
        return campo;
    }

    private VBox crearCampoConLabel(String texto, Node campo) {
        VBox box = new VBox(5);

        Label label = new Label(texto);
        label.getStyleClass().add("dialog-subtitle-label");

        box.getChildren().addAll(label, campo);

        return box;
    }
}