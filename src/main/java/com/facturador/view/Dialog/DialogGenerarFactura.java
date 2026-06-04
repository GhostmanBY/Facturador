package com.facturador.view.Dialog;

import java.util.List;
import java.util.Optional;

import com.facturador.utils.Utils;
import com.facturador.controller.ClienteController;
import com.facturador.controller.FacturaController;
import com.facturador.controller.ProductFController;
import com.facturador.model.Cliente;
import com.facturador.model.Factura;
import com.facturador.model.ProductFactura;
import com.facturador.model.User;
import com.facturador.view.Helpers.ErrorAlert;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DialogGenerarFactura {
    private ClienteController clienteController;
    private FacturaController facturaController;
    private ProductFController productFController;
    private ErrorAlert alert;
    private Utils utils;
    private Label lblTotalValor;
    private Label lblSubValor;
    private Label lblDescuentoValor;
    private Label lblImpuestosValor;
    private User user;
    private int IVA;

    private Runnable onRunable;

    public DialogGenerarFactura(
            Runnable oRunnable,
            Label lblTotalValor, 
            Label lblSubValor, 
            Label lblDescuentoValor, 
            Label lblImpuestosValor, 
            User user, 
            int IVA) {
        this.clienteController = new ClienteController();
        this.facturaController = new FacturaController();
        this.productFController = new ProductFController();
        this.utils = new Utils();
        this.lblTotalValor = lblTotalValor;
        this.lblSubValor = lblSubValor;
        this.lblDescuentoValor = lblDescuentoValor;
        this.lblImpuestosValor = lblImpuestosValor;
        this.user = user;
        this.IVA = IVA;
        this.onRunable = oRunnable;
    }

    public void configurarGenerarFactura(Button btnGenerar, ObservableList<ProductFactura> itemsFactura) {
        btnGenerar.setOnAction(e -> {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Generar Factura");

            DialogPane pane = dialog.getDialogPane();

            pane.getStylesheets().add(
                getClass().getResource("/assets/dialog.css").toExternalForm()
            );
            pane.getStyleClass().add("dialog-pane");

            Label lblClienteSection = new Label("CLIENTE");
            lblClienteSection.getStyleClass().add("dialog-field-label");

            ComboBox<String> cbClientes = new ComboBox<>();
            cbClientes.setPromptText("Seleccionar Cliente");
            this.clienteController.getClientes().forEach(c -> 
                cbClientes.getItems().add(c.getNombre() + " (ID: " + c.getId() + ")")
            );
            cbClientes.getStyleClass().add("dialog-combo");
            cbClientes.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(cbClientes, Priority.ALWAYS);

            Button btnCreateCliente = new Button("Crear Cliente");
            btnCreateCliente.setOnAction(ev -> {
                Dialog<Cliente> crearClienteView = new Dialog<>();
                crearClienteView.setTitle("Crear Cliente");

                TextField txtNombre = new TextField();
                TextField txtEmail = new TextField();
                TextField txtDocumento = new TextField();
                TextField txtDireccion = new TextField();
                TextField txtTelefono = new TextField();
                VBox content = new VBox(
                    10,
                    new Label("Nombre"),
                    txtNombre,
                    new Label("Email"),
                    txtEmail,
                    new Label("Documento"),
                    txtDocumento,
                    new Label("Direccion"),
                    txtDireccion,
                    new Label("Telefono"),
                    txtTelefono
                );

                crearClienteView.getDialogPane().setContent(content);
                ButtonType btnAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
                ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                crearClienteView.getDialogPane().getButtonTypes().addAll(
                    btnAceptar,
                    btnCancelar
                );

                Button btnAceptarReal = (Button) crearClienteView.getDialogPane().lookupButton(btnAceptar);
                btnAceptarReal.addEventFilter(ActionEvent.ACTION, event -> {
                    String nombre = txtNombre.getText().trim();
                    String email = txtEmail.getText().trim();
                    String documento = txtDocumento.getText().trim();
                    String direccion = txtDireccion.getText().trim();
                    String telefono = txtTelefono.getText().trim();

                    if (nombre.isBlank()) {
                        alert.mostrarError("El nombre no puede estar vacío");
                        event.consume();
                        return;
                    }

                    if (!this.utils.esEmailValido(email)) {
                        alert.mostrarError("Ingrese un email válido");
                        event.consume();
                        return;
                    }

                    if (!this.utils.esNumero(documento)) {
                        alert.mostrarError("El documento debe contener solo números");
                        event.consume();
                        return;
                    }

                    if (documento.length() < 7 || documento.length() > 10) {
                        alert.mostrarError("Documento inválido");
                        event.consume();
                        return;
                    }

                    if (direccion.isBlank()) {
                        alert.mostrarError("La dirección no puede estar vacía");
                        event.consume();
                        return;
                    }

                    if (!this.utils.esTelefonoValido(telefono)) {
                        alert.mostrarError("Teléfono inválido");
                        event.consume();
                    }
                });
                crearClienteView.setResultConverter(button -> {
                    if (button == btnAceptar) {
                        
                        return new Cliente.Builder()
                        .nombre(txtNombre.getText())
                        .email(txtEmail.getText())
                        .documento(txtDocumento.getText())
                        .direccion(txtDireccion.getText())
                        .telefono(txtTelefono.getText())
                        .isActive(true)
                        .build();
                    }


                    return null;
                });

                Optional<Cliente> result = crearClienteView.showAndWait();

                if (result.isPresent()) {
                    Cliente nuevo = result.get();
                    this.clienteController.createCliente(nuevo);
                    cbClientes.getItems().add(nuevo.getNombre() + " (ID: " + nuevo.getId() + ")");
                    cbClientes.getSelectionModel().selectLast();
                }
            });
            btnCreateCliente.getStyleClass().add("btn-sm");

            Label lblcli = new Label("Cliente: ");
            lblClienteSection.getStyleClass().add("dialog-field-label");

            HBox clienteRow = new HBox(10, lblcli, cbClientes, btnCreateCliente);
            clienteRow.setAlignment(Pos.CENTER_LEFT);

            VBox seccionCliente = new VBox(8, lblClienteSection, clienteRow);

            Separator sep = new Separator();
            sep.getStyleClass().add("dialog-sep");

            Label lblResumenSection = new Label("RESUMEN");
            lblResumenSection.getStyleClass().add("dialog-section-label");


            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            
            Label lblTotal = new Label("Total: ");
            TextField TxFtotal = new TextField(this.lblTotalValor.getText());

            VBox content_total = new VBox(lblTotal, TxFtotal);
            grid.add(content_total, 0, 0);

            Label lblSubtotal = new Label("Subtotal: ");
            TextField TxFsubtotal = new TextField(this.lblSubValor.getText());

            VBox content_subtotal = new VBox(lblSubtotal, TxFsubtotal);
            grid.add(content_subtotal, 0, 1);

            Label lblDescuento = new Label("Descuento: ");
            TextField TxFdescuento = new TextField(this.lblDescuentoValor.getText());

            VBox content_descuento = new VBox(lblDescuento, TxFdescuento);
            grid.add(content_descuento, 1, 0);

            Label lblImpuestos = new Label("Impuestos(" + this.IVA + "%): ");
            String limpio = this.lblImpuestosValor.getText().replaceAll("\\(.*?\\)", "");
            TextField TxFimpuestos = new TextField(limpio);

            TxFtotal.getStyleClass().addAll("dialog-field-input", "dialog-field-highlight");
            TxFtotal.setEditable(false);
            TxFsubtotal.getStyleClass().add("dialog-field-input");
            TxFsubtotal.setEditable(false);
            TxFdescuento.getStyleClass().add("dialog-field-input");
            TxFdescuento.setEditable(false);
            TxFimpuestos.getStyleClass().add("dialog-field-input");
            TxFimpuestos.setEditable(false);

            lblTotal.getStyleClass().add("dialog-field-label");
            lblSubtotal.getStyleClass().add("dialog-field-label");
            lblDescuento.getStyleClass().add("dialog-field-label");
            lblImpuestos.getStyleClass().add("dialog-field-label");

            VBox content_impuestos = new VBox(lblImpuestos, TxFimpuestos);
            grid.add(content_impuestos, 1, 1);

            VBox dialogContent = new VBox(20, seccionCliente, grid);
            dialogContent.setPadding(new Insets(20));
            dialog.getDialogPane().setContent(dialogContent);
            ButtonType btnAceptar = new ButtonType("Generar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(btnAceptar);
            Button btnAceptarReal = (Button) dialog.getDialogPane().lookupButton(btnAceptar);
            
            btnAceptarReal.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                if (cbClientes.getSelectionModel().isEmpty()) {
                    alert.mostrarError("Seleccione un cliente");
                    event.consume();
                    return;
                }

                int clienteId = Integer.parseInt(cbClientes.getSelectionModel().getSelectedItem().split("ID: ")[1].replace(")", ""));
                int vendedorId = this.user.getId(); // por ahora fijo

                double subtotal = itemsFactura.stream().mapToDouble(ProductFactura::getSubtotal).sum();
                double descuento = itemsFactura.stream().mapToDouble(p -> p.getCantidad() * p.getPrecioUnitario() * p.getDescuento() / 100).sum();
                double impuestos = (subtotal - descuento) * this.IVA / 100;
                double total = subtotal - descuento + impuestos;

                generarFactura(itemsFactura, subtotal, descuento, impuestos, total, clienteId, vendedorId);
            });

            dialog.showAndWait();
        });
    }

    private void generarFactura(List<ProductFactura> itemsFactura, double subtotal, double descuento, double impuestos, double total, int clienteId, int vendedorId) {
        Factura factura = Factura.builder()
        .vendedorId(vendedorId)
        .clienteId(clienteId)
        .subtotal(subtotal)
        .descuento(descuento)
        .impuestos(impuestos)
        .total(total)
        .detalles(itemsFactura)
        .build();
        Factura facturaCreada = this.facturaController.createFactura(factura);
        
        itemsFactura.replaceAll(item ->
            item.toBuilder()
            .facturaId(facturaCreada.getId())
            .build()
        );
        for (ProductFactura item : itemsFactura) {
            this.productFController.createDetailFactura(item);
        }

        itemsFactura.clear();
        if (this.onRunable != null) {
            onRunable.run();
        }
    }
}
