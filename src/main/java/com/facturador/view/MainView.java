package com.facturador.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import com.facturador.model.Producto;

import java.util.List;
import java.util.Optional;

import com.facturador.controller.StockController;
import com.facturador.model.ProductFactura;
import com.facturador.model.User;
import com.facturador.view.Tabs.TabProductos;
public class MainView {
    private TabProductos tabProductos;
    private StockController stockController;
    private VBox root;
    private ObservableList<Producto> datos;

    public MainView(Stage stage, User user) {
        this.stockController = new StockController();

        this.tabProductos = new TabProductos();

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
        tabFacturas.setContent(buildFacturasTab());

        TabPane tabPane = new TabPane(tabProductos, tabFacturas);
        tabPane.getStyleClass().add("tab-pane");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        root = new VBox(topbar, tabPane);
        root.getStyleClass().add("root");
    }

    private Parent buildFacturasTab() {
        TableView<Producto> tablaProductos = new TableView<>();
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

        ObservableList<ProductFactura> itemsFactura =
        FXCollections.observableArrayList();

        tablaFactura.setItems(itemsFactura);

        configurarAgregarProductos(
            tablaProductos,
            itemsFactura
        );

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar producto...");
        txtBuscar.getStyleClass().add("text-field");

        Label tituloProductos = new Label("Productos");
        tituloProductos.getStyleClass().add("label-title");

        Label tituloFactura = new Label("Factura");
        tituloFactura.getStyleClass().add("label-title");

        VBox panelProductos = new VBox(
            12,
            tituloProductos,
            txtBuscar,
            tablaProductos
        );

        panelProductos.getStyleClass().add("factura-panel");
        VBox.setVgrow(tablaProductos, Priority.ALWAYS);

        VBox panelFactura = new VBox(
            12,
            tituloFactura,
            tablaFactura
        );

        panelFactura.getStyleClass().add("factura-panel");
        VBox.setVgrow(tablaFactura, Priority.ALWAYS);

        SplitPane splitPane = new SplitPane(
            panelProductos,
            panelFactura
        );

        splitPane.setDividerPositions(0.5);

        Label lblSubtotal = new Label("Subtotal: $0");
        Label lblDescuento = new Label("Descuento: $0");
        Label lblImpuestos = new Label("Impuestos: $0");
        Label lblTotal = new Label("TOTAL: $0");

        lblSubtotal.getStyleClass().add("factura-resumen-item");
        lblDescuento.getStyleClass().add("factura-resumen-item");
        lblImpuestos.getStyleClass().add("factura-resumen-item");

        lblTotal.getStyleClass().add("factura-total");

        HBox resumen = new HBox(
            lblSubtotal,
            lblDescuento,
            lblImpuestos,
            lblTotal
        );
        resumen.getStyleClass().add("factura-resumen-bar");

        Button btnGenerar = new Button("Generar Factura");
        btnGenerar.getStyleClass().add("button");

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
    
    private void configurarAgregarProductos(TableView<Producto> tablaProductos, ObservableList<ProductFactura> itemsFactura) {
        tablaProductos.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Producto producto = row.getItem();
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setTitle("Agregar producto");

                    TextField txtCantidad = new TextField("1");
                    TextField txtDescuento = new TextField("0");
                    Label lblError = new Label();
                    lblError.setStyle("-fx-text-fill: red;");

                    VBox content = new VBox(
                        10,
                        new Label("Cantidad"),
                        txtCantidad,
                        new Label("Descuento (%)"),
                        txtDescuento,
                        lblError
                    );
                    dialog.getDialogPane().setContent(content);

                    ButtonType btnAgregar = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(btnAgregar, ButtonType.CANCEL);

                    Optional<ButtonType> result = dialog.showAndWait();

                    if (result.isEmpty() || result.get() != btnAgregar) {
                        return;
                    }

                    try {
                        int cantidad = Integer.parseInt(txtCantidad.getText());
                        double descuento = Double.parseDouble(txtDescuento.getText());

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

                            existente.toBuilder()
                            .cantidad(nuevaCantidad)
                            .build();

                            existente.toBuilder()
                            .subtotal(calcularSubtotal(nuevaCantidad, producto.getPrice(), existente.getDescuento()))
                            .build();

                            producto.toBuilder()
                            .stock(producto.getStock() - nuevaCantidad)
                            .build();

                            tablaProductos.refresh();
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

                            producto.toBuilder()
                            .stock(producto.getStock() - cantidad)
                            .build();
                            
                            itemsFactura.add(item);
                            tablaProductos.refresh();
                        }
                    } catch (NumberFormatException ex) {
                        mostrarError("Ingrese números válidos");
                    }
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

    public Parent getView() {
        return root;
    }
}