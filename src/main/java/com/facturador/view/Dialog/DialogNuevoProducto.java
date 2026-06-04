package com.facturador.view.Dialog;

import java.util.Optional;
import com.facturador.model.Producto;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DialogNuevoProducto {
    private final Producto productoExistente;

    public DialogNuevoProducto() {
        this.productoExistente = null;
    }

    public DialogNuevoProducto(Producto producto) {
        this.productoExistente = producto;
    }

    public Optional<Producto> abrirDialog() {
        boolean esEdicion = productoExistente != null;

        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle(esEdicion ? "Editar Producto" : "Nuevo Producto");
        dialog.setHeaderText(null);

        dialog.getDialogPane().getStylesheets().add(
            getClass().getResource("/assets/login.css").toExternalForm()
        );

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");
        txtNombre.getStyleClass().add("text-field");

        TextField txtDescripcion = new TextField();
        txtDescripcion.setPromptText("Descripción");
        txtDescripcion.getStyleClass().add("text-field");

        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Precio");
        txtPrecio.getStyleClass().add("text-field");

        TextField txtStock = new TextField();
        txtStock.setPromptText("Stock");
        txtStock.getStyleClass().add("text-field");

        Label lblError = new Label("");
        lblError.getStyleClass().add("error-label");

        if (esEdicion) {
            txtNombre.setText(productoExistente.getName());
            txtDescripcion.setText(productoExistente.getDescription());
            txtPrecio.setText(String.valueOf(productoExistente.getPrice()));
            txtStock.setText(String.valueOf(productoExistente.getStock()));
        }

        VBox form = new VBox(12,
            new Label("Nombre"),      txtNombre,
            new Label("Descripción"), txtDescripcion,
            new Label("Precio"),      txtPrecio,
            new Label("Stock"),       txtStock,
            lblError
        );
        //form.getStyleClass().add("card-form");
        form.setPrefWidth(350);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);
        dialog.getDialogPane().setContent(form);

        // Sin Platform.runLater, directo acá
        Button botonGuardar = (Button) dialog.getDialogPane().lookupButton(btnGuardar);
        botonGuardar.getStyleClass().add("button");
        botonGuardar.setDisable(true);

        txtNombre.textProperty().addListener((obs, old, val) ->
            botonGuardar.setDisable(val.isBlank() || txtPrecio.getText().isBlank())
        );
        txtPrecio.textProperty().addListener((obs, old, val) ->
            botonGuardar.setDisable(val.isBlank() || txtNombre.getText().isBlank())
        );

        Button botonCancelar = (Button) dialog.getDialogPane().lookupButton(btnCancelar);
        botonCancelar.getStyleClass().add("button-danger");

        dialog.setResultConverter(buttonType -> {
            if (buttonType == btnGuardar) {
                try {
                    Producto.Builder builder = Producto.builder()
                        .name(txtNombre.getText().trim())
                        .description(txtDescripcion.getText().trim())
                        .price(Double.parseDouble(txtPrecio.getText().trim().replace(",", ".")))
                        .stock(Integer.parseInt(txtStock.getText().trim()));

                    if (esEdicion) {
                        builder.id(productoExistente.getId())
                        .code(productoExistente.getCode());
                    }

                    return builder.build();
                } catch (NumberFormatException ex) {
                    lblError.setText("Precio y stock deben ser números");
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }
}