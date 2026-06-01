package com.facturador.view;

import java.util.List;

import com.facturador.controller.StockController;
import com.facturador.model.Producto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.facturador.model.User;

public class MainView {
    private StockController stockController;
    private VBox root;
    private ObservableList<Producto> datos;

    public MainView(Stage stage, User user) {
        this.stockController = new StockController();

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
        tabProductos.setContent(buildProductosTab());

        Tab tabFacturas = new Tab("Facturas");
        tabFacturas.setClosable(false);
        tabFacturas.setContent(new Label("Próximamente..."));

        TabPane tabPane = new TabPane(tabProductos, tabFacturas);
        tabPane.getStyleClass().add("tab-pane");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        root = new VBox(topbar, tabPane);
        root.getStyleClass().add("root");
    }

    private VBox buildProductosTab() {
        TableView<Producto> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.getStyleClass().add("table-view");

        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        TableColumn<Producto, String> colDesc = new TableColumn<>("Descripción");
        colDesc.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));

        TableColumn<Producto, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));

        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()).asObject());

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());
        
        tabla.getColumns().add(colNombre);
        tabla.getColumns().add(colDesc);
        tabla.getColumns().add(colCodigo);
        tabla.getColumns().add(colPrecio);
        tabla.getColumns().add(colStock);

        List<Producto> listado = this.stockController.getStock(0, 20);
        datos = FXCollections.observableArrayList(listado);
        tabla.setItems(datos);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        TextField buscador = new TextField();
        buscador.setPromptText("Buscar Producto...");
        buscador.getStyleClass().add("text-field");

        Button btnAgregar = new Button("+ Nuevo producto");
        btnAgregar.getStyleClass().add("button");
        btnAgregar.setOnAction(e -> {
                DialogViewNewProducto dialog = new DialogViewNewProducto();
                dialog.abrirDialog().ifPresent( producto -> {
                        this.stockController.createStock(producto);
                        datos.setAll(stockController.getStock(0, 20));
                    }
                );
            }
        );

        ContextMenu contextMenu = new ContextMenu();

        MenuItem editar = new MenuItem("Editar");
        MenuItem eliminar = new MenuItem("Eliminar");

        contextMenu.getItems().addAll(editar, eliminar);

        tabla.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnContextMenuRequested(e -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, e.getScreenX(), e.getScreenY());
                }
            });
            return row;
        });

        editar.setOnAction(e -> {
            Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                DialogViewNewProducto dialog = new DialogViewNewProducto(seleccionado);
                dialog.abrirDialog().ifPresent( producto -> {
                        this.stockController.modifyStock(producto);
                        datos.setAll(stockController.getStock(0, 20));
                    }
                );
            }
        });

        eliminar.setOnAction(e -> {
            Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                
            }
        });

        HBox toolbar = new HBox(12, buscador, btnAgregar);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(12, 16, 8, 16));
        toolbar.getStyleClass().add("tab-toolbar");
        HBox.setHgrow(buscador, Priority.ALWAYS);

        VBox content = new VBox(toolbar, tabla);
        VBox.setVgrow(content, Priority.ALWAYS);
        content.getStyleClass().add("tab-content");

        return content;
    }

    public Parent getView() {
        return root;
    }
}