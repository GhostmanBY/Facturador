package com.facturador.view.Tabs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facturador.controller.ProveedoreController;
import com.facturador.controller.StockController;
import com.facturador.model.Producto;
import com.facturador.model.Proveedores;
import com.facturador.view.Dialog.DialogNuevoProducto;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TabProductos {
    private StockController stockController;
    private ProveedoreController proveedoreController;
    private VBox root;
    private ObservableList<Producto> datos;
    private TableView<Producto> tabla = new TableView<>();
    private Map<Integer, String> proveedorMap;

    public TabProductos() {
        this.stockController = new StockController();
        this.proveedoreController = new ProveedoreController();
    }

    public VBox buildProductosTab() {
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.getStyleClass().add("table-view");

        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        TableColumn<Producto, String> colDesc = new TableColumn<>("Descripción");
        colDesc.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));

        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()).asObject());

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());

        cargarMapaProveedores();

        TableColumn<Producto, String> colProveedor = new TableColumn<>("Proveedor");
        colProveedor.setCellValueFactory(c ->
            new SimpleStringProperty(proveedorMap.getOrDefault(c.getValue().getProveedorId(), "N/A"))
        );

        tabla.getColumns().add(colNombre);
        tabla.getColumns().add(colDesc);
        tabla.getColumns().add(colPrecio);
        tabla.getColumns().add(colStock);
        tabla.getColumns().add(colProveedor);

        List<Producto> listado = this.stockController.getStock();
        datos = FXCollections.observableArrayList(listado);
        tabla.setItems(datos);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        TextField buscador = new TextField();
        buscador.setPromptText("Buscar Producto...");
        buscador.getStyleClass().add("text-field");
        buscador.textProperty().addListener((obs, oldText, newText) -> {
            String filtro = newText.toLowerCase();
            ObservableList<Producto> filtrados = FXCollections.observableArrayList();

            for (Producto p : datos) {
                if (p.getName().toLowerCase().contains(filtro) || p.getDescription().toLowerCase().contains(filtro)) {
                    filtrados.add(p);
                }
            }

            tabla.setItems(filtrados);
        });

        Button btnAgregar = new Button("+ Nuevo producto");
        btnAgregar.getStyleClass().add("button");
        btnAgregar.setOnAction(e -> {
                DialogNuevoProducto dialog = new DialogNuevoProducto();
                dialog.abrirDialog().ifPresent( producto -> {
                        this.stockController.createStock(producto);
                        datos.setAll(stockController.getStock());
                    }
                );
            }
        );

        ContextMenu contextMenu = new ContextMenu();

        MenuItem editar = new MenuItem("Editar");
        MenuItem activar = new MenuItem("Activar");
        MenuItem desactivar = new MenuItem("desactivar");

        
        tabla.setRowFactory(tv -> new TableRow<>() {
            {
                setOnContextMenuRequested(e -> {
                    if (!isEmpty()) {
                        Producto producto = getItem();
                        contextMenu.getItems().clear();
                        contextMenu.getItems().add(editar);

                        if (producto.getIsActive()) {
                            contextMenu.getItems().add(desactivar);
                        } else {
                            contextMenu.getItems().add(activar);
                        }
                        contextMenu.show(this,e.getScreenX(), e.getScreenY());
                    }
                });
            }
            @Override
            protected void updateItem(Producto item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().remove("row-inactivo");

                if (!empty && item != null && !item.getIsActive()) {
                    getStyleClass().add("row-inactivo");
                }
            }
        });

        editar.setOnAction(e -> {
            Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                DialogNuevoProducto dialog = new DialogNuevoProducto(seleccionado);
                dialog.abrirDialog().ifPresent( producto -> {
                        this.stockController.modifyStock(producto);
                        datos.setAll(stockController.getStock());
                    }
                );
            }
        });

        activar.setOnAction(e -> {
            Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                this.stockController.activateStock(seleccionado.getId());
                datos.setAll(stockController.getStock());
            }
        });

        desactivar.setOnAction(e -> {
            Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                this.stockController.deactivateStock(seleccionado.getId());
                datos.setAll(stockController.getStock());
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

    private void cargarMapaProveedores() {
        proveedorMap = new HashMap<>();
        for (Proveedores p : proveedoreController.getProveedores()) {
            proveedorMap.put(p.getId(), p.getNombre());
        }
    }
    
    public void recargarProductos() {
        List<Producto> listado = this.stockController.getStock();
        datos = FXCollections.observableArrayList(listado);
        tabla.setItems(datos);
        cargarMapaProveedores();
    }
}
