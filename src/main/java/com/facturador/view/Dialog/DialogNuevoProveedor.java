package com.facturador.view.Dialog;

import java.util.Optional;

import com.facturador.controller.ProveedoreController;
import com.facturador.model.Proveedores;
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

public class DialogNuevoProveedor {
    private ErrorAlert errorAlert = new ErrorAlert();
    private ProveedoreController proveedoreController;
    private final Proveedores proveedorExistente;

    public DialogNuevoProveedor() {
        this.proveedorExistente = null;
        this.proveedoreController = new ProveedoreController();
    }

    public DialogNuevoProveedor(Proveedores proveedor) {
        this.proveedorExistente = proveedor;
        this.proveedoreController = new ProveedoreController();
    }

    public Optional<Proveedores> mostrarDialogo() {
        boolean esEdicion = proveedorExistente != null;

        Dialog<Proveedores> dialogo = new Dialog<>();
        dialogo.setTitle(esEdicion ? "Editar Proveedor" : "Nuevo Proveedor");

        DialogPane panel = dialogo.getDialogPane();
        panel.getStylesheets().add(
            getClass().getResource("/assets/dialog.css").toExternalForm()
        );
        panel.getStyleClass().add("dialog-pane");

        TextField tpNombre = crearCampoTexto("Ej: Distribuidora Norte");
        TextField tpCuit = crearCampoTexto("Ej: 30-12345678-9");
        TextField tpDireccion = crearCampoTexto("Ej: Av. Colón 1234");
        TextField tpTelefono = crearCampoTexto("Ej: 2234556677");
        TextField tpEmail = crearCampoTexto("Ej: ventas@proveedor.com");

        if (esEdicion) {
            tpNombre.setText(proveedorExistente.getNombre());
            tpCuit.setText(proveedorExistente.getCuit());
            tpDireccion.setText(proveedorExistente.getDireccion());
            tpTelefono.setText(proveedorExistente.getTelefono());
            tpEmail.setText(proveedorExistente.getEmail());
        }

        GridPane grilla = new GridPane();
        grilla.setHgap(12);
        grilla.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        grilla.getColumnConstraints().addAll(col1, col2);

        grilla.add(crearCampoConLabel("Nombre", tpNombre), 0, 0);
        grilla.add(crearCampoConLabel("CUIT", tpCuit), 1, 0);
        grilla.add(crearCampoConLabel("Dirección", tpDireccion), 0, 1);
        grilla.add(crearCampoConLabel("Teléfono", tpTelefono), 1, 1);
        grilla.add(crearCampoConLabel("Email", tpEmail), 0, 2, 2, 1);

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.getStyleClass().add("btn-primary");
        btnCancelar.getStyleClass().add("btn-ghost");

        HBox filaBotones = new HBox(8, btnCancelar, btnGuardar);
        filaBotones.setAlignment(Pos.CENTER_RIGHT);

        VBox contenido = new VBox(16, grilla, filaBotones);
        panel.setContent(contenido);

        panel.getButtonTypes().add(ButtonType.CANCEL);

        Button oculto = (Button) panel.lookupButton(ButtonType.CANCEL);
        oculto.setVisible(false);
        oculto.setManaged(false);

        Proveedores[] resultado = {null};

        btnCancelar.setOnAction(e -> dialogo.close());

        btnGuardar.setOnAction(e -> {
            String nombre = tpNombre.getText().trim();
            String cuit = tpCuit.getText().trim();
            String direccion = tpDireccion.getText().trim();
            String telefono = tpTelefono.getText().trim();
            String email = tpEmail.getText().trim();

            if (nombre.isBlank()) {
                errorAlert.mostrarError("El nombre no puede estar vacío");
                return;
            }
            if (cuit.isBlank()) {
                errorAlert.mostrarError("El CUIT no puede estar vacío");
                return;
            }
            if (telefono.isBlank()) {
                errorAlert.mostrarError("El teléfono no puede estar vacío");
                return;
            }

            Proveedores.Builder builder = Proveedores.builder()
                .nombre(nombre)
                .cuit(cuit)
                .direccion(direccion)
                .telefono(telefono)
                .email(email)
                .isActive(true);

            if (esEdicion) {
                builder.id(proveedorExistente.getId());
                Proveedores editado = builder.build();
                proveedoreController.updateProveedore(editado);
                resultado[0] = editado;
            } else {
                Proveedores nuevo = builder.build();
                proveedoreController.createProveedore(nuevo);
                resultado[0] = nuevo;
            }
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
