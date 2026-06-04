package com.facturador.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import com.facturador.model.Producto;

import com.facturador.view.Tabs.TabProductos;
import com.facturador.view.Tabs.TabFacturas;

import java.util.List;
import java.util.Optional;

import com.facturador.controller.StockController;
import com.facturador.controller.FacturaController;
import com.facturador.controller.ProductFController;
import com.facturador.controller.ClienteController;
import com.facturador.model.ProductFactura;
import com.facturador.model.User;
import com.facturador.model.Cliente;
import com.facturador.model.DProFactura;
import com.facturador.model.Factura;
import com.facturador.utils.Utils;

public class MainView {
    private TabProductos tabProductos;
    private TabFacturas tabfacturas;
    private StockController stockController;
    private FacturaController facturaController;
    private ProductFController productFController;
    private ClienteController clienteController;
    private Utils utils;
    private VBox root;
    private ObservableList<Producto> datos;
    private static final int IVA = 21;
    private Label lblSubValor = new Label("$ 0.00");
    private Label lblDescuentoValor = new Label("$ 0.00");
    private Label lblImpuestosValor = new Label("$ 0.00"+ " (" + IVA + "%" + ")");
    private Label lblTotalValor = new Label("$ 0.00");
    private ObservableList<ProductFactura> itemsFactura;
    private TableView<Producto> tablaProductos;
    private User user;

    public MainView(Stage stage, User user) {
        this.stockController = new StockController();
        this.facturaController = new FacturaController();
        this.productFController = new ProductFController();
        this.clienteController = new ClienteController();
        this.utils = new Utils();
        this.tabProductos = new TabProductos();
        this.tabfacturas = new TabFacturas(user);

        Label userName = new Label(user.getName());
        userName.getStyleClass().add("label-title");

        Label userRole = new Label(user.getRole());
        userRole.getStyleClass().add("label-subtitle");

        Button btn_logout = new Button("Cerrar Sesion");
        btn_logout.getStyleClass().add("button-danger");
        btn_logout.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            stage.getScene().setRoot(loginView.getView());
        });

        VBox userInfo = new VBox(userName, userRole);
        userInfo.setAlignment(Pos.CENTER_LEFT);

        VBox logout = new VBox(btn_logout);
        logout.setAlignment(Pos.CENTER_RIGHT);

        HBox topbar = new HBox(userInfo, logout);
        topbar.getStyleClass().add("topbar");
        topbar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(userInfo, Priority.ALWAYS);

        Tab tabProductos = new Tab("Productos");
        tabProductos.setClosable(false);
        tabProductos.setContent(this.tabProductos.buildProductosTab());

        Tab tabFacturas = new Tab("Facturas");
        tabFacturas.setClosable(false);
        tabFacturas.setContent(this.tabfacturas.buildFacturasTab());

        TabPane tabPane = new TabPane(tabProductos, tabFacturas);
        tabPane.getStyleClass().add("tab-pane");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        root = new VBox(topbar, tabPane);
        root.getStyleClass().add("root");
    }

    private Parent buildFacturasTab() {
        tablaProductos = new TableView<>();
        tablaProductos.getStyleClass().add("table-view");
        
        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());
        TableColumn<Producto, Double> colPrice = new TableColumn<>("Precio");
        colPrice.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()).asObject());

        tablaProductos.getColumns().add(colNombre);
        tablaProductos.getColumns().add(colStock);
        tablaProductos.getColumns().add(colPrice);

        List<Producto> listado = this.stockController.getStock(0, 20);
        datos = FXCollections.observableArrayList(listado);
        tablaProductos.setItems(datos);
        VBox.setVgrow(tablaProductos, Priority.ALWAYS);


        TableView<ProductFactura> tablaFactura = new TableView<>();
        tablaFactura.getStyleClass().add("table-view");

        TableColumn<ProductFactura, String> colNombreF = new TableColumn<>("Nombre");
        colNombreF.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        TableColumn<ProductFactura, Integer> colCantidad = new TableColumn<>("Cant.");
        colCantidad.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getCantidad()).asObject());

        TableColumn<ProductFactura, Double> colUnitario = new TableColumn<>("P. Unit.");
        colUnitario.setCellValueFactory(c ->new SimpleDoubleProperty(c.getValue().getPrecioUnitario()).asObject());

        TableColumn<ProductFactura, Double> colDesc = new TableColumn<>("Desc %");
        colDesc.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getDescuento()).asObject());

        TableColumn<ProductFactura, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getSubtotal()).asObject());

        tablaFactura.getColumns().add(colNombreF);
        tablaFactura.getColumns().add(colCantidad);
        tablaFactura.getColumns().add(colUnitario);
        tablaFactura.getColumns().add(colDesc);
        tablaFactura.getColumns().add(colSubtotal);

        itemsFactura = FXCollections.observableArrayList();

        tablaFactura.setItems(itemsFactura);

        configurarAgregarProductos(
            tablaProductos,
            itemsFactura
        );
        
        configurarEditarProductos(
            tablaFactura,
            itemsFactura
        );

        Label tituloProductos = new Label("Productos");
        tituloProductos.getStyleClass().add("label-title");

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar producto...");
        txtBuscar.getStyleClass().add("text-field");
        txtBuscar.setAlignment(Pos.CENTER_LEFT);
        
        HBox searchBox = new HBox(tituloProductos, txtBuscar);
        searchBox.getStyleClass().add("search-box");
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefHeight(70);
        
        HBox.setHgrow(txtBuscar, Priority.ALWAYS);
        txtBuscar.setMaxWidth(Double.MAX_VALUE);
        HBox.setMargin(tituloProductos, new Insets(0, 0, 0, 10));
        HBox.setMargin(txtBuscar, new Insets(10, 10, 10, 10));
        txtBuscar.textProperty().addListener((obs, oldText, newText) -> {
            String filtro = newText.toLowerCase();
            ObservableList<Producto> filtrados = FXCollections.observableArrayList();

            for (Producto p : datos) {
                if (p.getName().toLowerCase().contains(filtro)) {
                    filtrados.add(p);
                }
            }

            tablaProductos.setItems(filtrados);
        });

        VBox panelProductos = new VBox(
            searchBox,
            tablaProductos
        );
        panelProductos.getStyleClass().add("factura-panel");
        
        VBox.setVgrow(tablaProductos, Priority.ALWAYS);
        
        Label tituloFactura = new Label("Factura");
        tituloFactura.getStyleClass().add("label-title");
        tituloFactura.setAlignment(Pos.CENTER_LEFT);

        HBox headerFactura = new HBox(
            tituloFactura,
            new Region() // espacio vacío
        );
        HBox.setMargin(tituloFactura, new Insets(0, 0, 0, 10));
        headerFactura.setAlignment(Pos.CENTER_LEFT);
        headerFactura.setPrefHeight(70);

        VBox panelFactura = new VBox(
            headerFactura,
            tablaFactura
        );

        panelFactura.getStyleClass().add("factura-panel");
        VBox.setVgrow(tablaFactura, Priority.ALWAYS);

        SplitPane splitPane = new SplitPane(
            panelProductos,
            panelFactura
        );

        splitPane.setDividerPositions(0.5);

        Label lblSubtotal = new Label("Subtotal: ");
        lblSubValor.getStyleClass().add("factura-values");
        HBox subtotalBox = new HBox(lblSubtotal, lblSubValor);
        subtotalBox.getStyleClass().add("factura-resumen-item");

        Label lblDescuento = new Label("Descuento: ");
        lblDescuentoValor.getStyleClass().add("factura-values");
        HBox descuentoBox = new HBox(lblDescuento, lblDescuentoValor);
        descuentoBox.getStyleClass().add("factura-resumen-item");

        Label lblImpuestos = new Label("Impuestos: ");
        lblImpuestosValor.getStyleClass().add("factura-values");
        HBox impuestosBox = new HBox(lblImpuestos, lblImpuestosValor);
        impuestosBox.getStyleClass().add("factura-resumen-item");

        Label lblTotal = new Label("TOTAL: ");
        lblTotalValor.getStyleClass().add("factura-total-value");
        HBox totalBox = new HBox(lblTotal, lblTotalValor);
        totalBox.getStyleClass().add("factura-total");

        lblSubtotal.getStyleClass().add("factura-resumen-item");
        lblDescuento.getStyleClass().add("factura-resumen-item");
        lblImpuestos.getStyleClass().add("factura-resumen-item");

        lblTotal.getStyleClass().add("factura-total");

        HBox resumen = new HBox(
            subtotalBox,
            descuentoBox,
            impuestosBox,
            totalBox
        );
        resumen.getStyleClass().add("factura-resumen-bar");

        Button btnGenerar = new Button("Generar Factura");
        btnGenerar.getStyleClass().add("button");

        configurarGenerarFactura(
            btnGenerar,
            itemsFactura
        );

        HBox.setHgrow(resumen, Priority.ALWAYS);

        HBox footer = new HBox(
            resumen,
            btnGenerar
        );

        footer.setAlignment(Pos.CENTER_LEFT);
        footer.getStyleClass().add("factura-footer");

        BorderPane root = new BorderPane();
        root.setCenter(splitPane);
        root.setBottom(footer);

        return root;
    }

    private Optional<DProFactura> mostrarDialogoProducto(String titulo, int cantidadInicial, double descuentoInicial) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(titulo);

        TextField txtCantidad = new TextField(String.valueOf(cantidadInicial));
        TextField txtDescuento = new TextField(String.valueOf(descuentoInicial));

        VBox content = new VBox(
            10,
            new Label("Cantidad"),
            txtCantidad,
            new Label("Descuento (%)"),
            txtDescuento
        );

        dialog.getDialogPane().setContent(content);

        ButtonType btnAceptar =
            new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes()
            .addAll(btnAceptar, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isEmpty() || result.get() != btnAceptar) {
            return Optional.empty();
        }

        try {
            return Optional.of(
                new DProFactura(
                    Integer.parseInt(txtCantidad.getText()),
                    Double.parseDouble(txtDescuento.getText())
                )
            );
        } catch (NumberFormatException e) {
            mostrarError("Ingrese números válidos");
            return Optional.empty();
        }
    }
    
    private void configurarAgregarProductos(TableView<Producto> tablaProductos, ObservableList<ProductFactura> itemsFactura) {
        tablaProductos.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Producto producto = row.getItem();
                    Optional<DProFactura> dialog = mostrarDialogoProducto(
                        "Agregar a Factura: " + producto.getName(),
                        1,
                        0
                    );

                    if (dialog.isEmpty()) {
                        return;
                    }

                    try {
                        int cantidad = dialog.get().cantidad();
                        double descuento = dialog.get().descuento();


                        if (cantidad <= 0) {
                            mostrarError("La cantidad debe ser mayor a cero");
                            return;
                        }

                        if (cantidad > producto.getStock()) {
                            mostrarError("Stock insuficiente");
                            return;
                        }

                        if (descuento < 0 || descuento > 100) {
                            mostrarError("El descuento debe estar entre 0 y 100");
                            return;
                        }

                        ProductFactura existente = itemsFactura.stream()
                            .filter(p -> p.getProductoid() == producto.getId())
                            .findFirst()
                            .orElse(null);

                        if (existente != null) {
                            int nuevaCantidad = existente.getCantidad() + cantidad;

                            if (nuevaCantidad > producto.getStock()) {
                                mostrarError("Stock insuficiente");
                                return;
                            }

                            int index = itemsFactura.indexOf(existente);

                            ProductFactura actualizado = existente.toBuilder()
                            .cantidad(nuevaCantidad)
                            .subtotal(
                                calcularSubtotal(
                                    nuevaCantidad,
                                    producto.getPrice(),
                                    existente.getDescuento()
                                )
                            )
                            .build();
                        
                            itemsFactura.set(index, actualizado);
                        } else {
                            ProductFactura item = ProductFactura.builder()
                                .productoid(producto.getId())
                                .cantidad(cantidad)
                                .name(producto.getName())
                                .precioUnitario(producto.getPrice())
                                .descuento(descuento)
                                .subtotal(calcularSubtotal(cantidad, producto.getPrice(), descuento))
                                .isActive(true)
                                .build();                            
                            itemsFactura.add(item);
                        }
                        
                        actualizarResumenFactura();
                        tablaProductos.refresh();
                        actualizarProductos(producto, cantidad);
                    } catch (NumberFormatException ex) {
                        mostrarError("Ingrese números válidos");
                    }
                }
            });
            return row;
        });
    }

    private void configurarEditarProductos(TableView<ProductFactura> tablaFactura, ObservableList<ProductFactura> itemsFactura) {
        tablaFactura.setRowFactory(tv -> {
            TableRow<ProductFactura> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    ProductFactura item = row.getItem();
                    Optional<DProFactura> dialog =
                        mostrarDialogoProducto(
                            "Editar: " + item.getName(),
                            item.getCantidad(),
                            item.getDescuento()
                        );

                    if (dialog.isEmpty()) {
                        return;
                    }

                    int cantidad = dialog.get().cantidad();
                    double descuento = dialog.get().descuento();

                    ProductFactura actualizado = item.toBuilder()
                        .cantidad(cantidad)
                        .descuento(descuento)
                        .subtotal(
                            calcularSubtotal(
                                cantidad,
                                item.getPrecioUnitario(),
                                descuento
                            )
                        )
                        .build();

                    int index = itemsFactura.indexOf(item);
                    itemsFactura.set(index, actualizado);

                    actualizarResumenFactura();
                    tablaFactura.refresh();
                    actualizarProductos(
                        this.stockController.getStockById(item.getProductoid()),
                        cantidad - item.getCantidad() // diferencia entre nueva y vieja cantidad
                    );
                }
            });
            return row;
        });
    }

    private double calcularSubtotal(int cantidad, double precio,double descuento) {
        double subtotal = cantidad * precio;
        return subtotal - (subtotal * descuento / 100);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void actualizarProductos(Producto producto, int diferencia) {
        ObservableList<Producto> items = tablaProductos.getItems();
        for (int i = 0; i < items.size(); i++) {
            Producto actual = items.get(i);
            if (actual.getId() == producto.getId()) {
                Producto actualizado = actual.toBuilder()
                    .stock(actual.getStock() - diferencia)
                    .build();
                items.set(i, actualizado);
                break;
            }
        }
    }

    private void configurarGenerarFactura(Button btnGenerar, ObservableList<ProductFactura> itemsFactura) {
        btnGenerar.setOnAction(e -> {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Generar Factura");

            ComboBox<String> cbClientes = new ComboBox<>();
            cbClientes.setPromptText("Seleccionar Cliente");
            this.clienteController.getClientes().forEach(c -> 
                cbClientes.getItems().add(c.getNombre() + " (ID: " + c.getId() + ")")
            );

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
                        mostrarError("El nombre no puede estar vacío");
                        event.consume();
                        return;
                    }

                    if (!this.utils.esEmailValido(email)) {
                        mostrarError("Ingrese un email válido");
                        event.consume();
                        return;
                    }

                    if (!this.utils.esNumero(documento)) {
                        mostrarError("El documento debe contener solo números");
                        event.consume();
                        return;
                    }

                    if (documento.length() < 7 || documento.length() > 10) {
                        mostrarError("Documento inválido");
                        event.consume();
                        return;
                    }

                    if (direccion.isBlank()) {
                        mostrarError("La dirección no puede estar vacía");
                        event.consume();
                        return;
                    }

                    if (!this.utils.esTelefonoValido(telefono)) {
                        mostrarError("Teléfono inválido");
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

            HBox content = new HBox(10, new Label("Cliente: "), cbClientes, btnCreateCliente);
            content.setAlignment(Pos.CENTER_LEFT);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            
            Label lblTotal = new Label("Total: ");
            lblTotal.getStyleClass().add("label-title");

            TextField TxFtotal = new TextField(lblTotalValor.getText());
            TxFtotal.getStyleClass().add("label-title");

            VBox content_total = new VBox(lblTotal, TxFtotal);
            grid.add(content_total, 0, 0);

            Label lblSubtotal = new Label("Subtotal: ");
            lblSubtotal.getStyleClass().add("label-title");

            TextField TxFsubtotal = new TextField(lblSubValor.getText());
            TxFsubtotal.getStyleClass().add("label-title");

            VBox content_subtotal = new VBox(lblSubtotal, TxFsubtotal);
            grid.add(content_subtotal, 0, 1);

            Label lblDescuento = new Label("Descuento: ");
            lblDescuento.getStyleClass().add("label-title");

            TextField TxFdescuento = new TextField(lblDescuentoValor.getText());
            TxFdescuento.getStyleClass().add("label-title");

            VBox content_descuento = new VBox(lblDescuento, TxFdescuento);
            grid.add(content_descuento, 1, 0);


            Label lblImpuestos = new Label("Impuestos(" + IVA + "%): ");
            lblImpuestos.getStyleClass().add("label-title");

            String limpio = lblImpuestosValor.getText().replaceAll("\\(.*?\\)", "");
            TextField TxFimpuestos = new TextField(limpio);
            TxFimpuestos.getStyleClass().add("label-title");

            VBox content_impuestos = new VBox(lblImpuestos, TxFimpuestos);
            grid.add(content_impuestos, 1, 1);

            VBox dialogContent = new VBox(20, content, grid);
            dialogContent.setPadding(new Insets(20));
            dialog.getDialogPane().setContent(dialogContent);
            ButtonType btnAceptar = new ButtonType("Generar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(btnAceptar);
            Button btnAceptarReal = (Button) dialog.getDialogPane().lookupButton(btnAceptar);
            
            btnAceptarReal.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                if (cbClientes.getSelectionModel().isEmpty()) {
                    mostrarError("Seleccione un cliente");
                    event.consume();
                    return;
                }

                int clienteId = Integer.parseInt(cbClientes.getSelectionModel().getSelectedItem().split("ID: ")[1].replace(")", ""));
                int vendedorId = this.user.getId(); // por ahora fijo

                double subtotal = itemsFactura.stream().mapToDouble(ProductFactura::getSubtotal).sum();
                double descuento = itemsFactura.stream().mapToDouble(p -> p.getCantidad() * p.getPrecioUnitario() * p.getDescuento() / 100).sum();
                double impuestos = (subtotal - descuento) * IVA / 100;
                double total = subtotal - descuento + impuestos;

                generarFactura(itemsFactura, subtotal, descuento, impuestos, total, clienteId, vendedorId);
            });

            dialog.showAndWait();
        });
    }

    private void actualizarResumenFactura() {
        double subtotal = 0;
        double descuento = 0;

        for (ProductFactura item : itemsFactura) {
            double bruto = item.getCantidad() * item.getPrecioUnitario();

            subtotal += bruto;
            descuento += bruto * item.getDescuento() / 100;
        }

        double neto = subtotal - descuento;

        double impuestos = neto * 21.0 / 100; // por ahora
        double total = subtotal - descuento + impuestos;

        lblSubValor.setText(String.format("$ %.2f", subtotal));
        lblImpuestosValor.setText(String.format("$ %.2f", impuestos) + " (" + IVA + "%)");
        lblTotalValor.setText(String.format("$ %.2f", total));
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
    }

    public Parent getView() {
        return root;
    }
}