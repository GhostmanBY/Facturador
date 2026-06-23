package com.facturador.view.Dialog;

import java.util.List;
import java.util.Optional;

import com.facturador.controller.ProveedoreController;
import com.facturador.controller.StockController;
import com.facturador.model.Producto;
import com.facturador.model.Proveedores;
import com.facturador.view.Helpers.ErrorAlert;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DialogNuevoProducto {
    private final Producto productoExistente;
    private ErrorAlert alert;
    private ProveedoreController proveedoreController;
    private StockController stockController;

    public DialogNuevoProducto() {
        this.productoExistente = null;
        this.alert = new ErrorAlert();
        this.proveedoreController = new ProveedoreController();
        this.stockController = new StockController();
    }

    public DialogNuevoProducto(Producto producto) {
        this.productoExistente = producto;
        this.alert = new ErrorAlert();
        this.proveedoreController = new ProveedoreController();
        this.stockController = new StockController();
    }

    public Optional<Producto> abrirDialog() {
        boolean esEdicion = productoExistente != null;

        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle(esEdicion ? "Editar Producto" : "Nuevo Producto");
        dialog.setHeaderText(null);

        DialogPane panel = dialog.getDialogPane();
        panel.getStylesheets().add(
            getClass().getResource("/assets/dialog.css").toExternalForm()
        );
        panel.getStyleClass().add("dialog-pane");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Coca Cola 500ml");
        txtNombre.getStyleClass().add("dialog-text-field");

        TextField txtDescripcion = new TextField();
        txtDescripcion.setPromptText("Ej: Gaseosa sabor cola");
        txtDescripcion.getStyleClass().add("dialog-text-field");

        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Ej: 1200.00");
        txtPrecio.getStyleClass().add("dialog-text-field");

        TextField txtStock = new TextField();
        txtStock.setPromptText("Ej: 50");
        txtStock.getStyleClass().add("dialog-text-field");

        ComboBox<String> cbProveedores = new ComboBox<>();
        cbProveedores.setPromptText("Seleccionar Proveedor");
        cbProveedores.getStyleClass().add("dialog-combo");
        cbProveedores.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cbProveedores, Priority.ALWAYS);

        cargarProveedores(cbProveedores);

        if (esEdicion) {
            txtNombre.setText(productoExistente.getName());
            txtDescripcion.setText(productoExistente.getDescription());
            txtPrecio.setText(String.valueOf(productoExistente.getPrice()));
            txtStock.setText(String.valueOf(productoExistente.getStock()));
            if (productoExistente.getProveedorId() > 0) {
                for (String item : cbProveedores.getItems()) {
                    if (item.contains("ID: " + productoExistente.getProveedorId())) {
                        cbProveedores.getSelectionModel().select(item);
                        break;
                    }
                }
            }
        }

        Label lblError = new Label("");
        lblError.getStyleClass().add("error-label");

        GridPane grilla = new GridPane();
        grilla.setHgap(12);
        grilla.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        grilla.getColumnConstraints().addAll(col1, col2);

        grilla.add(crearCampoConLabel("Nombre", txtNombre), 0, 0);
        grilla.add(crearCampoConLabel("Precio", txtPrecio), 1, 0);
        grilla.add(crearCampoConLabel("Descripción", txtDescripcion), 0, 1);
        grilla.add(crearCampoConLabel("Stock", txtStock), 1, 1);

        Button btnNuevoProveedor = new Button("Nuevo Proveedor");
        btnNuevoProveedor.getStyleClass().add("btn-sm");
        btnNuevoProveedor.setOnAction(ev -> {
            DialogNuevoProveedor dialogProveedor = new DialogNuevoProveedor();
            Optional<Proveedores> result = dialogProveedor.mostrarDialogo();
            if (result.isPresent()) {
                Proveedores nuevo = result.get();
                cbProveedores.getItems().add(nuevo.getNombre() + " (ID: " + nuevo.getId() + ")");
                cbProveedores.getSelectionModel().selectLast();
            }
        });

        HBox proveedorRow = new HBox(10, cbProveedores, btnNuevoProveedor);
        proveedorRow.setAlignment(Pos.CENTER_LEFT);

        VBox proveedorBox = crearCampoConLabel("Proveedor", proveedorRow);

        VBox contenido = new VBox(16, grilla, proveedorBox, lblError);
        contenido.setPadding(new Insets(20));
        panel.setContent(contenido);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        panel.getButtonTypes().addAll(btnGuardar, btnCancelar);

        Button botonGuardar = (Button) panel.lookupButton(btnGuardar);
        botonGuardar.getStyleClass().add("btn-primary");

        Button botonCancelar = (Button) panel.lookupButton(btnCancelar);
        botonCancelar.getStyleClass().add("btn-ghost");

        if (!esEdicion) {
            botonGuardar.setDisable(true);
            txtNombre.textProperty().addListener((obs, old, val) ->
                botonGuardar.setDisable(val.isBlank() || txtPrecio.getText().isBlank())
            );
            txtPrecio.textProperty().addListener((obs, old, val) ->
                botonGuardar.setDisable(val.isBlank() || txtNombre.getText().isBlank())
            );
        }

        botonGuardar.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                int stock = Integer.parseInt(txtStock.getText().trim());
                double precio = Double.parseDouble(txtPrecio.getText().trim().replace(",", "."));
                if (stock <= 0) {
                    this.alert.mostrarError("La cantidad debe ser mayor a cero");
                    event.consume();
                    return;
                }
                if (precio < 0) {
                    this.alert.mostrarError("El precio no puede ser negativo");
                    event.consume();
                    return;
                }
            } catch (NumberFormatException ex) {
                lblError.setText("Precio y stock deben ser números");
                event.consume();
            }
        });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == btnGuardar) {
                if (!esEdicion){
                    boolean existe = this.stockController.getStock()
                        .stream()
                        .anyMatch(producto ->
                            producto.getName().equalsIgnoreCase(txtNombre.getText().trim())
                        );

                    if (existe) {
                        this.alert.mostrarError("No se puede crear un producto con el mismo nombre");
                        return null;
                    }
                }

                int proveedorId = 0;
                String selected = cbProveedores.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    proveedorId = Integer.parseInt(selected.split("ID: ")[1].replace(")", ""));
                }

                Producto.Builder builder = Producto.builder()
                    .proveedorId(proveedorId)
                    .name(txtNombre.getText().trim())
                    .description(txtDescripcion.getText().trim())
                    .price(Double.parseDouble(txtPrecio.getText().trim().replace(",", ".")))
                    .stock(Integer.parseInt(txtStock.getText().trim()));

                if (esEdicion) {
                    builder.id(productoExistente.getId());
                }
                return builder.build();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void cargarProveedores(ComboBox<String> cb) {
        for (Proveedores p : proveedoreController.getProveedores()) {
            if (p.isActive()) {
                cb.getItems().add(p.getNombre() + " (ID: " + p.getId() + ")");
            }
        }
    }

    private VBox crearCampoConLabel(String texto, Node campo) {
        VBox box = new VBox(5);
        Label label = new Label(texto);
        label.getStyleClass().add("dialog-subtitle-label");
        box.getChildren().addAll(label, campo);
        return box;
    }
}
